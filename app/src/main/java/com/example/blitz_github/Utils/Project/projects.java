package com.example.blitz_github.Utils.Project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class projects extends AppCompatActivity {

    private static final String KEY_TITLE = "title";
    private static final String KEY_CLIENT = "client";
    private static final String KEY_ROLE = "role";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";


    private TextView start,end;
    private EditText role,client,title,description;
    private FirebaseFirestore firestore;
    private ProgressDialog progress;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        start = findViewById(R.id.project_start);
        end = findViewById(R.id.project_end);
        role = findViewById(R.id.project_role);
        title = findViewById(R.id.project_title);
        description = findViewById(R.id.project_desc);
        client = findViewById(R.id.project_client);

        UID = FirebaseAuth.getInstance().getUid();

        firestore = FirebaseFirestore.getInstance();

        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getDate(start);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                getDate(end);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDate(final TextView textView)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        textView.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                },
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void submitProject(View view) {

        String titleText = title.getText().toString();
        String clientText = client.getText().toString();
        String roleText = role.getText().toString();
        String descText = description.getText().toString();
        String startText = start.getText().toString();
        String endText = end.getText().toString();


        //check if any field is empty or not

//        projectModel obj = new projectModel(titleText,clientText,roleText,descText,startText,endText);

//new map
        if(!(TextUtils.isEmpty(titleText)) && !(TextUtils.isEmpty(roleText)) && !(TextUtils.isEmpty(clientText)) && !(TextUtils.isEmpty(descText)) && !(startText.equals("Select Start Date")) && !(endText.equals("Select End Date")))
        {
            Map<String, Object> obj = new HashMap<>();
            obj.put(KEY_TITLE,titleText);
            obj.put(KEY_CLIENT,clientText);
            obj.put(KEY_ROLE,roleText);
            obj.put(KEY_START,startText);
            obj.put(KEY_END,endText);
            obj.put(KEY_DESCRIPTION,descText);





            progress=new ProgressDialog(this);
            progress.setTitle("Uploading Data");
            progress.setCancelable(false);
            progress.show();

            firestore.collection("users").document(UID).collection("Projects").document(titleText).set(obj)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(projects.this, "Upload Done", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            startActivity(new Intent(projects.this, Complete_Registration.class));//new
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(projects.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    });
        }
        else {
            Toast.makeText(projects.this,"Please fill all the fields and select the dates.",Toast.LENGTH_SHORT).show();
        }

    }
}

