package com.example.blitz_github.dialoguebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blitz_github.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeySkillActivity extends AppCompatActivity {

    private AutoCompleteTextView editTextkeyskill;
    private String skillstring;
    private TextView skilldisplay;
    private Button addButton,saveButton;
    private ListView skillList;
    private ArrayList<String> arrayList;
    private ArrayAdapter adapter;
    String[] skillsArr=new String[]{"Java","C++","Kotlin","C","Android studio","Android",
            "Python","Management","Leader", "Html", "CSS", "Javascript","Designer" ,"Graphic Designer", "SQL", "Marketing",
            "Dedicated", "Fast Learner", "Data Structures", "Photoshop", "Adobe XD","Oracle","MySQL"};

//    public KeySkillActivity() {
//        //no argument constructor
//    }
//
//    public KeySkillActivity(String skillstring, TextView skilldisplay) {
//        this.skillstring=skillstring;
//        this.skilldisplay=skilldisplay;
//    }
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UID = FirebaseAuth.getInstance().getUid();
        setContentView(R.layout.keyskill_dialoguebox);
        editTextkeyskill=findViewById(R.id.edittextskill_id);
        addButton=findViewById(R.id.skilladdbutton_id);
        saveButton=findViewById(R.id.skillsavebutton_id);
        skillList=findViewById(R.id.skilllistview_id);
        arrayList= new ArrayList<String>();
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        skillList.setAdapter(adapter);
        editTextkeyskill.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,skillsArr));

        Bundle bundle=getIntent().getExtras();
        skillstring=bundle.getString("skillstring");

        //converting the skills string in skill section in completeregistration part to arraylist for listview to make new edit
        Log.i("TAG", "onCreate: "+skillstring);
        ArrayList<String> oldskillList=new ArrayList<String>(Arrays.asList(skillstring.split(" ")));
        for(String x: oldskillList)
        {
            arrayList.add(x);
            adapter.notifyDataSetChanged();
        }


        skillList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(KeySkillActivity.this)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                arrayList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null )
                        .show();


                return true;
            }
        });


    }
    public  void addSkill(View view){
        String skillText= editTextkeyskill.getText().toString().trim();
        if(skillText.isEmpty())
        {
            editTextkeyskill.setError("Add a Skill First.");
        }
        else{
            arrayList.add(skillText);
            adapter.notifyDataSetChanged();
            editTextkeyskill.setText("");
        }
    }
    public void saveSkill(View view){

        //converting listview arraylist values to string to save to firebase
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading Data");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String skill="";
        for(String s: arrayList)
        {
            skill+=s+" ";
        }
        Log.i("TAG", "saveSkill: "+skill);
        Map<String,Object> data = new HashMap<>();
        data.put("Skill",skill);
        //add skill to firebase here
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final String finalSkill = skill;
        firebaseFirestore.collection("users").document(UID).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("keyskills", finalSkill);
                        setResult(2323,intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(KeySkillActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}