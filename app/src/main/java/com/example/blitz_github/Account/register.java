package com.example.blitz_github.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blitz_github.Home_activity;
import com.example.blitz_github.R;
import com.example.blitz_github.Utils.CustomLogin;
import com.example.blitz_github.Utils.loginHelper;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    private final int AUTO_LOGIN = 9672;
    private Button button;
    private EditText register_email;
    private  EditText register_password;
    private EditText register_name;
    private EditText register_mob;
    FirebaseAuth mFirebaseAuth;
    register context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFirebaseAuth = FirebaseAuth.getInstance();
        register_email=findViewById(R.id.user_email);
        register_password=findViewById(R.id.user_password);
        register_name=findViewById(R.id.user_name);
        register_mob=findViewById(R.id.user_mob);
        button=findViewById(R.id.button);
        context = this;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=register_email.getText().toString();
                String Password=register_password.getText().toString();
                String phone=register_mob.getText().toString();
                final String Name = register_name.getText().toString();
                String MobilePattern="[0-9]{10}";

                if(Name.isEmpty()){
                    register_name.setError("This field can't be empty");
                    register_name.requestFocus();
                }
                else if(mail.isEmpty()){
                    register_email.setError("This field can't be empty");
                    register_email.requestFocus();
                }
                else if(Password.isEmpty()) {
                    register_password.setError("This field can't be empty");
                    register_password.requestFocus();
                }
                   else if(!phone.matches(MobilePattern) || phone.length()!=10) {
                    register_mob.setError("The Phone number is Invalid");
                    register_mob.requestFocus();
                }

                else if(!(Name.isEmpty() && mail.isEmpty() && Password.isEmpty() && phone.isEmpty())) {

                    CustomLogin customLogin = new CustomLogin();
                    customLogin.createAccount(mail,Password,Name,phone,register.this);
                    customLogin.setMyListener(new CustomLogin.MyListener() {
                        @Override
                        public void onDataLoaded() {
                            context.startActivity(new Intent(context,login_register.class));
                            context.finish();
                        }
                    });
                }
                else {
                    Toast.makeText(register.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


//abc
    public void googleLogin(View view) {
        Intent intent = new Intent(register.this, loginHelper.class);
        intent.putExtra("Way","Google");
        startActivityForResult(intent,AUTO_LOGIN);
    }

    public void facebookLogin(View view) {
        Intent intent = new Intent(register.this,
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
                Intent intent=new Intent(this, Complete_Registration.class);
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
                Intent intent=new Intent(this, Home_activity.class);
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