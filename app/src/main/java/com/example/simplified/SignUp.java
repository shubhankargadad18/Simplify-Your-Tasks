package com.example.simplified;

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

import com.google.firebase.auth.FirebaseAuth;


public class SignUp extends AppCompatActivity {
    EditText name,email,pass,c_pass;
    Button register;
    TextView t;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputEmail);
        pass = findViewById(R.id.InputPassword);
        c_pass = findViewById(R.id.InputConfirmPassword);
        register = findViewById(R.id.btnLogin);
        t = findViewById(R.id.gotoRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();
                String Name = name.getText().toString().trim();
                String cPass = c_pass.getText().toString().trim();
                if (TextUtils.isEmpty(nEmail)) {
                    email.setError("Required Field...");
                }
                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("Required Field...");
                }
                if (TextUtils.isEmpty(Name)) {
                    pass.setError("Required Field...");
                }
                if (TextUtils.isEmpty(cPass)) {
                    pass.setError("Required Field...");
                }
                mDialog.setMessage("Processing...");
                mDialog.show();
                mAuth.createUserWithEmailAndPassword(nEmail, mPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(SignUp.this, HomeActivity.class);
                        startActivity(in);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Problem", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                });
            }
        });
    }
}