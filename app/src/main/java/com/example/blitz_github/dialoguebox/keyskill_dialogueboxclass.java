package com.example.blitz_github.dialoguebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.blitz_github.R;

public class keyskill_dialogueboxclass extends AppCompatDialogFragment {

    private AutoCompleteTextView editTextkeyskill;
    private String skillstring;
    private  TextView skilllist,skillfulllist;
    private Button editbutton;

    String[] skillsArr=new String[]{"Java","java","JAVA","cpp","c++","Cpp","c","C","Android studio","Android",
                "Python","management","leader", "Html", "CSS", "Javascript"};




    public  keyskill_dialogueboxclass(String skillstring,TextView skilllist){
        this.skillstring=skillstring;
        this.skilllist=skilllist;

    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.keyskill_dialoguebox, null);



        builder.setView(view)
                .setTitle("Enter Your Skill")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("TAG", "onClick: 111111111111111111111111111111111111");
                       skilllist.setText("");
                        Log.i("TAG", "onClick:2222222222222222222222222222222222222222222 ");
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String newskill= editTextkeyskill.getText().toString();
                        //this will update the textview in completeregistration section
                        if(!newskill.isEmpty())
                        {newskill+= ", "+skillstring;
                        skilllist.setText(newskill);}
                    }
                });

        editTextkeyskill=view.findViewById(R.id.edittextskill_id);
        //skillfulllist=view.findViewById(R.id.skillfulllist_id);
        skillfulllist.setText(skillstring);

        editTextkeyskill.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,skillsArr));



        return builder.create();

    }

}
