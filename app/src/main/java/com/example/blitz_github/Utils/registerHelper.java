package com.example.blitz_github.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

public class registerHelper {

    StorageReference storageReference;
    Context context;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progress;
    private MyListener listener;

    public interface MyListener{
        void onDataLoaded();
        void isUserRegistered(boolean isPresent);
        void renderData(DocumentSnapshot snapshot);
    }
    public registerHelper(Context context){
        this.context=context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progress  = new ProgressDialog(context);
        listener = null;
    }
    public void setListener(MyListener listener){
        this.listener=listener;
    }
    private void upload(Uri URI, final String Uid, String file_type, final String File){

        progress.setTitle("Uploading "+File);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();
        final StorageReference resume_path = storageReference.child("Users "+File+"s").child(Uid+"."+file_type);
        Log.v("SubString",URI.toString().substring(0,3));
        if(URI.toString().substring(0,4).equals("http"))
        {
            progress.dismiss();
        }
        else{
        resume_path.putFile(URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                resume_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        firebaseFirestore.collection("users").document(Uid).update(File+"_url",uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, File+" Added to Server", Toast.LENGTH_SHORT).show();
                                        if(File=="Resume")
                                        progress.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });}
    }

    public void Upload_Resume(Uri URI, final String Uid){
        upload(URI,Uid,"pdf","Resume");
    }

    public void Upload_Data(Map<String,Object> data,String Uid,Uri uri)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setMessage("Your Internet is slow please wait");
            }
        },6000);
       upload(uri,Uid,"jpg","Image");
       addData(data,Uid);
    }

    private void addData(Map<String,Object> data,String Uid){

        progress.setTitle("Uploading Data");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();

        firebaseFirestore.collection("users").document(Uid).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onDataLoaded();
                        progress.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }

    public void markUpload(String Uid) {

        Map<String ,Object> data = new HashMap<>();
        data.put("Data Present",true);
        data.put("User Id",Uid);
        firebaseFirestore.collection("users metadata").document(Uid).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void checkUserData(String uid) {

        progress.setTitle("Checking Data");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();

        firebaseFirestore.collection("users metadata").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()&&documentSnapshot.contains("Data Present"))
                        {
                            listener.isUserRegistered(true);
                        }
                        else
                        {
                            listener.isUserRegistered(false);
                        }
                        progress.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.isUserRegistered(false);
                        progress.dismiss();
                    }
                });

    }

    public void getDataToRender(String uid) {
        progress.setTitle("Checking Data");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();

        firebaseFirestore.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists())
                        {
                            listener.renderData(documentSnapshot);
                        }
                        else
                        {
                            listener.renderData(null);
                        }
                        progress.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.renderData(null);
                        progress.dismiss();
                    }
                });
    }

}
