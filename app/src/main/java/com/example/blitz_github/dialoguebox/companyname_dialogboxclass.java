package com.example.blitz_github.dialoguebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.blitz_github.R;

public class companyname_dialogboxclass extends AppCompatDialogFragment{
    private EditText editTextkeycompany;
    private String companynamestring;
    private TextView companyname;



    public  companyname_dialogboxclass(String companynamestring, TextView companyname){
        this.companynamestring=companynamestring;
        this.companyname=companyname;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.companyname_dialogbox, null);



        builder.setView(view)
                .setTitle("Enter Company Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String newcompanyname= editTextkeycompany.getText().toString();
                        //this will update the textview in completeregistration section
                        companyname.setText(newcompanyname);
                    }
                });

        editTextkeycompany=view.findViewById(R.id.editcompanyname);
        editTextkeycompany.setText(companynamestring);

        return builder.create();

    }
}