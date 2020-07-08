package com.example.blitz_github.dialoguebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.blitz_github.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Projectdialogboxclass extends AppCompatDialogFragment {

    private EditText projectDes;
    private String projectstring;
    private TextView projectlist;

    public Projectdialogboxclass(String projectstring, TextView projectlist) {
        this.projectstring = projectstring;
        this.projectlist = projectlist;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.projects_dialogbox, null);

        builder.setView(view)
                .setTitle("Enter Your Project Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String newproject= projectDes.getText().toString();

                        //textview in completeregistration section
                        projectlist.setText(newproject);

                    }
                });

        projectDes=view.findViewById(R.id.editproject);
        projectDes.setText(projectstring);

        return builder.create();

    }

}
