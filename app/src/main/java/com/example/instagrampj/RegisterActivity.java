package com.example.instagrampj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText userName;
    private EditText fullName;
    private EditText email;
    private EditText password;
    private Button registerBtn;
    private TextView loginText;
    ProgressDialog pd;

    FirebaseFirestore fStore;
    String userId;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.userName);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        loginText = findViewById(R.id.loginText);

        fAuth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);


//        To Toggle between login and register
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

//        To register the user in firebase database
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUsername = userName.getText().toString();
                String textName = fullName.getText().toString();
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (TextUtils.isEmpty(textUsername) || TextUtils.isEmpty(textName) || TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Fill form properly!", Toast.LENGTH_SHORT).show();
                }else if(textPassword.length()<5) {
                    Toast.makeText(RegisterActivity.this, "Weak password!", Toast.LENGTH_SHORT).show();
                }else {
                    registerUser(textUsername, textName, textEmail, textPassword);
                }

            }
        });
    }

    private void registerUser(String textUsername, String textName, String textEmail, String textPassword) {
        pd.setMessage("Please Wait!");
        pd.show();
        fAuth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    FirebaseUser fUser = fAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Register successfully done!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Onfailure: Email Not Sent" + e.getMessage());
                        }
                    });

                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                    userId = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("user").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("fUserName",textUsername);
                    user.put("fFullName",textName);
                    user.put("fMail",textEmail);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onsuccess: user profile is created for" + userId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }else{
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT) ;
                }
            }
        });
    }
}