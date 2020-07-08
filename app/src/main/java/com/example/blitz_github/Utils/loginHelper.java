package com.example.blitz_github.Utils;
//This is a Ghost Activity without layout
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blitz_github.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class loginHelper extends AppCompatActivity {

    private int GoogleOrFacebook_SIGN_IN = 9672;
    String Way="";
    private CustomLogin customLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private LoginManager loginManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_helper);

        firebaseAuth = FirebaseAuth.getInstance();

        //Preprocessing for Google
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        customLogin = new CustomLogin();

        Intent intent = getIntent();
        Way = intent.getStringExtra("Way");
        Log.v("Way::",Way);
        if(Way.equals("Facebook"))
        {
            loginManager = LoginManager.getInstance();
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //Here we also get the token of login
                    Log.v("FacebookLog--",loginResult.toString());
                    AccessToken token  = loginResult.getAccessToken();
                    CreateUser(token.getToken());
                }

                @Override
                public void onCancel() {
                    Log.v("FacebookLogCancel","Cancel");
                    returnError("Cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.v("FacebookLogError",error.toString());
                    returnError(error.toString());
                }
            });
        }
        else
        {
            signIn();//This is for Google
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GoogleOrFacebook_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GoogleOrFacebook_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String token = account.getIdToken();
                CreateUser(token);
            } catch (ApiException e) {
                String error = e.getMessage();
                Log.v("Message",e.toString());
                returnError(error);
            }
        }
        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void returnError(String error){
        Intent data = new Intent();
        data.putExtra("Result",error);
        setResult(100,data);
        finish();
    }

    private void CreateUser(String token){

        customLogin.LoginWithGoogleorFacebook(token,this,Way,this);

    }


}