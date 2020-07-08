package com.example.blitz_github.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blitz_github.Home_activity;
import com.example.blitz_github.R;
import com.example.blitz_github.Utils.CustomLogin;
import com.example.blitz_github.Utils.loginHelper;
import com.example.blitz_github.dialoguebox.forgetpassword_dialogboxclass;
import com.example.blitz_github.dialoguebox.username_dialogboxclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class login_register extends AppCompatActivity {

    private final int AUTO_LOGIN = 9672;
    login_register context;
    EditText emailEdit;
    EditText passwordEdit;
    Button button;
    TextView registerButton;
    TextView forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_login_register);
        emailEdit= findViewById(R.id.Email_Text);
        passwordEdit = findViewById(R.id.Password_Text);
        button=findViewById(R.id.button);
        registerButton= findViewById(R.id.registernow);
        forgetPassword = findViewById(R.id.forgetPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                if(email.isEmpty())
                {
                    emailEdit.setError("Field can't be empty.");
                }
                if(password.isEmpty())
                {
                    passwordEdit.setError("Field can't be empty.");
                }
                if(!(email.isEmpty()) && !(password.isEmpty()))
                {
                    CustomLogin objCost= new CustomLogin();
                    objCost.LoginAccount(email,password,login_register.this);
                    objCost.setMyListener(new CustomLogin.MyListener() {
                        @Override
                        public void onDataLoaded() {
                            context.startActivity(new Intent(context,Complete_Registration.class));
                            context.finish();
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,register.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                The user should get a dialog box here where he is asked to enter the email id and after entering as he click ok button in dialog box.
                you need to call the resetPassword function below with email id and give a toast to user as well to go and check his/her mail.
                */
                forgetpassword_dialogboxclass forgetpasswordclass=new forgetpassword_dialogboxclass();

                forgetpasswordclass.setupListener(new forgetpassword_dialogboxclass.Mylisterner() {
                    @Override
                    public void resetPassword(String Email) {
                        Toast.makeText(context, "Please Enter Your Email Address To Reset Your Password", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                    }
                });

                forgetpasswordclass.show(getSupportFragmentManager(), "forget password");

            }
        });

    }

    public void googleLogin(View view) {
        Intent intent = new Intent(login_register.this, loginHelper.class);
        intent.putExtra("Way","Google");
        startActivityForResult(intent,AUTO_LOGIN);
    }

    public void facebookLogin(View view) {
        Intent intent = new Intent(login_register.this,
                loginHelper.class);
        intent.putExtra("Way","Facebook");
        startActivityForResult(intent,AUTO_LOGIN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTO_LOGIN)
        {
            if(resultCode==200){
                Intent intent=new Intent(login_register.this, Complete_Registration.class);
                intent.putExtra("From login",true);
                startActivity(intent);
                finish();
            }
            else if(resultCode==100)
            {
                String result=data.getStringExtra("Result");
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==300)
            {
                Intent intent=new Intent(login_register.this, Home_activity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(context, "Some Error Occur", Toast.LENGTH_SHORT).show();
            }
        }
    }

}