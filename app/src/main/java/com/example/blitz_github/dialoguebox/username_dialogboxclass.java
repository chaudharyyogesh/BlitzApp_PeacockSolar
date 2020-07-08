package com.example.blitz_github.dialoguebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blitz_github.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class username_dialogboxclass extends AppCompatDialogFragment {

    private EditText editTextkeyname;
    private String usernamestring;
    private TextView username;



    public  username_dialogboxclass(String usernamestring,TextView username){
        this.usernamestring=usernamestring;
        this.username=username;

    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.username_dialogbox, null);



        builder.setView(view)
                .setTitle("Enter Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String newname= editTextkeyname.getText().toString();
                        //this will update the textview in completeregistration section
                        username.setText(newname);
                    }
                });

        editTextkeyname=view.findViewById(R.id.editusername);
        editTextkeyname.setText(usernamestring);

        return builder.create();

    }
}