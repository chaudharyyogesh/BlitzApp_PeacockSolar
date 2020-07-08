package com.example.blitz_github;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.Account.login_register;
import com.example.blitz_github.Account.register;
import com.example.blitz_github.Utils.loginHelper;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    FirebaseAuth firebaseAuth;
    int AUTO_LOGIN=9672;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_sceen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);

            }
        },SPLASH_TIME_OUT);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //check for login here and if already login move to homeActivity
        CheckUserExist();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTO_LOGIN)
        {
            if(resultCode==200){
                Login();
            }
            else if(resultCode==100)
            {
                String result=data.getStringExtra("Result");
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
            else if(resultCode==300)
            {
                Intent intent=new Intent(MainActivity.this, Home_activity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "Some Error Occur", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void CheckUserExist() {

        firebaseAuth = FirebaseAuth.getInstance();//For custom Login
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);//For Google Login
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();//For facebook Login
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(account!=null //|| isLoggedIn
        ) {
            Login();
        }
        else if(firebaseAuth.getCurrentUser()!=null)
        {
            if(firebaseAuth.getCurrentUser().isEmailVerified())
                Login();
        }
    }
    private void Login(){
        Intent intent=new Intent(MainActivity.this, Complete_Registration.class);
        intent.putExtra("extraData","Data");
        startActivity(intent);
        finish();
    }

    public void googleLogin(View view) {
        Intent intent = new Intent(MainActivity.this,loginHelper.class);
        intent.putExtra("Way","Google");
        startActivityForResult(intent,AUTO_LOGIN);
    }



    public void registerPage(View view) {
        startActivity(new Intent(this, register.class));
    }

    public void Pagelogin(View view) {
        startActivity(new Intent(this, login_register.class));
    }
}