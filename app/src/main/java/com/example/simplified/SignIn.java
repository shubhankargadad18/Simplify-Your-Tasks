package com.example.simplified;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private EditText email,password;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        mDialog = new ProgressDialog(this);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.InputPassword);
        Button login = findViewById(R.id.btnLogin);
        TextView register = findViewById(R.id.gotoRegister);
        register.setOnClickListener(view -> {
            Intent in = new Intent(SignIn.this,SignUp.class);
            startActivity(in);
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nEmail = email.getText().toString().trim();
                String nPass = password.getText().toString().trim();
                if(TextUtils.isEmpty(nEmail)){
                    email.setError("Required Field...");
                }
                if(TextUtils.isEmpty(nPass)){
                    password.setError("Required Field...");
                }
                mDialog.setMessage("Processing...");
                mDialog.show();
                mAuth.signInWithEmailAndPassword(nEmail,nPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            mDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}