package com.example.blitz_github.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.Account.register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

class AddData {

    AlertDialog.Builder builder;

    public void byGoogleOrFacebook(FirebaseUser user, final loginHelper activity, final ProgressDialog progressDialog){
        if(user==null)
        {Log.v("User:","User NUll");return;}
        else
            Log.v("User:",user.toString());
        progressDialog.setTitle("Entering Data");
        String name = user.getDisplayName();
        String mobile = user.getPhoneNumber();
        String email = user.getEmail();
        String photouri = user.getPhotoUrl().toString()+"?type=large";
        final String uid = user.getUid();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String, Object> User = new HashMap<>();
        User.put("name", name);
        User.put("mobile", mobile);
        User.put("email", email);
        User.put("Image_url",photouri);
        Log.v("Map=", User.toString());
        // Add a new document with a generated ID

        //before adding check if already present

        db.collection("users metadata").document(uid).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                db.collection("users").document(uid).set(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.v("upper","not found in meatdata");
                        progressDialog.dismiss();
                        activity.setResult(200);
                        activity.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddData", "Error adding document", e);
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if(snapshot.exists()){
                Log.v("lower","Found in meta data");
                progressDialog.dismiss();
                activity.setResult(300);
                activity.finish();}
                else
                {
                    db.collection("users").document(uid).set(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.v("upper","not found in meatdata");
                            progressDialog.dismiss();
                            activity.setResult(200);
                            activity.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("AddData", "Error adding document", e);
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });


    }

    public void byCustom(String name, String mobile, String email, String uid, final ProgressDialog progress, final Context context, final CustomLogin.MyListener listener){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> User = new HashMap<>();
        User.put("name", name);
        User.put("mobile", mobile);
        User.put("email", email);
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Note")
                .setMessage("Please Verify Your Mail and then Login")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progress.dismiss();
                        listener.onDataLoaded();
                    }
                });
        progress.setTitle("Entering Data");

        db.collection("users").document(uid).set(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.setMessage("DataAdded");

                builder.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

public class CustomLogin{

    public interface MyListener{
        void onDataLoaded();
    }
    FirebaseAuth mauth;
    ProgressDialog progress;
    private MyListener listener;
    public CustomLogin(){
        mauth=FirebaseAuth.getInstance();
        listener=null;
    }

    public void setMyListener(MyListener listener){
        this.listener = listener;
    }

    public void createAccount(final String Email, String Password, final String name, final String Mobile, final Context context)
    {
        progress=new ProgressDialog(context);
        progress.setTitle("Creating Account");
        progress.setCancelable(false);
        progress.show();
        mauth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final FirebaseUser user = mauth.getCurrentUser();

                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AddData addData = new AddData();
                                        addData.byCustom(name,Mobile,Email,user.getUid(),progress,context,listener);
                                    }else
                                    {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                    progress.dismiss();
                                }
                            });
                }
                else
                {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }

            }
        });
    }

    public void LoginAccount(String Email, String Password, final Context context)
    {
        progress=new ProgressDialog(context);
        progress.setTitle("Creating Account");
        progress.setCancelable(false);
        progress.show();
        mauth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    boolean proceed = mauth.getCurrentUser().isEmailVerified();
                    if(proceed)
                    {
                        progress.dismiss();
                        listener.onDataLoaded();
                    }
                    else{
                        Toast.makeText(context, "Please Verify Your Email on mail", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }
    public void LoginWithGoogleorFacebook(String idToken, loginHelper helper, String Way, final loginHelper context)
    {
        progress=new ProgressDialog(context);
        progress.setTitle("Creating Account");
        progress.setCancelable(false);
        progress.show();

        AuthCredential credential;

        //Choosing Credentials
        if(Way.equals("Facebook")){
            credential = FacebookAuthProvider.getCredential(idToken);
        }
        else{
        credential = GoogleAuthProvider.getCredential(idToken, null);}

        Log.d("Credentials::::",credential.toString());

        mauth.signInWithCredential(credential)
                .addOnCompleteListener(helper, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            AddData addData = new AddData();
                            addData.byGoogleOrFacebook(user,context,progress);

                        } else {
                            // If sign in fails, display a message to the user.
                            progress.dismiss();
                            Log.v("Failed google","Failed with login on google or facebook");
                            Intent intent = new Intent();
                            intent.putExtra("Result",task.getException().getMessage());
                            context.setResult(100,intent);
                            context.finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Failed google",e.getMessage());
                progress.dismiss();
            }
        });


    }
    public void finish(Complete_Registration obj)
    {
        obj.finish();
    }
}
