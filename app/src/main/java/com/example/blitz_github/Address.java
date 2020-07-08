package com.example.blitz_github;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blitz_github.Account.login_register;
import com.example.blitz_github.Account.register;
import com.example.blitz_github.Utils.CustomLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Address extends AppCompatActivity {

    private Button button;
    private EditText house_no;
    private  EditText street;
    private EditText city;
    private EditText postalCode;
    private EditText state;
    private Map<String,Object> data = new HashMap<>();
    private String UID;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        house_no=findViewById(R.id.houseNo);
        street=findViewById(R.id.StreetName);
        city=findViewById(R.id.cityName);
        postalCode=findViewById(R.id.PostalAddress);
        state=findViewById(R.id.stateName);
        button=findViewById(R.id.button3);
        UID = FirebaseAuth.getInstance().getUid();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String House=house_no.getText().toString();
                final String Street=street.getText().toString();
                final String City=city.getText().toString();
                final String Postal = postalCode.getText().toString();
                final String State = state.getText().toString();

                String PostalPattern="[0-9]{6}";

                if(House.isEmpty()){
                    house_no.setError("This field can't be empty");
                    house_no.requestFocus();
                }
                else if(Street.isEmpty()){
                    street.setError("This field can't be empty");
                    street.requestFocus();
                }
                else if(City.isEmpty()) {
                    city.setError("This field can't be empty");
                    city.requestFocus();
                }
                else if(!Postal.matches(PostalPattern) || Postal.length()!=6) {
                    postalCode.setError("The Postal Code is Invalid");
                    postalCode.requestFocus();
                }
                else if(State.isEmpty()) {
                    state.setError("This field can't be empty");
                    state.requestFocus();
                }

                else if(!(House.isEmpty() && Street.isEmpty() && City.isEmpty() && Postal.isEmpty()&& State.isEmpty())) {

                    //Here we will add code to submit address
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("Uploading Data");
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();
                    data.put("House",House);data.put("Street",Street);data.put("City",City);data.put("Postal",Postal);data.put("State",State);
                    firestore.collection("users").document(UID).collection("Address").document("data").set(data)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Address.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<String,Object> data = new HashMap<>();
                                    Log.v("trydata","someupload");
                                    String val = House+", "+Street+", "+City+", "+State+"-"+Postal;
                                    data.put("Address",val);
                                    putother(data,val);

                                }
                            });
                }
                else {
                    Toast.makeText(Address.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String data = getIntent().getExtras().getString("Address");
        if (data!=null&&!data.isEmpty())
        {
            String address[] = data.split(", ");
            String house = address[0];
            String streets = address[1];
            String citys = address[2];
            String both[] = address[3].split("-");
            String states = both[0];String postals = both[1];
            house_no.setText(house);
            street.setText(streets);
            state.setText(states);
            city.setText(citys);
            postalCode.setText(postals);
        }
    }

    private void putother(Map<String,Object> data, String val) {
        firestore.collection("users").document(UID).update(data);
        Toast.makeText(Address.this, "Data uploaded", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("showThis",val);
        setResult(2000,intent);
        finish();
    }
}