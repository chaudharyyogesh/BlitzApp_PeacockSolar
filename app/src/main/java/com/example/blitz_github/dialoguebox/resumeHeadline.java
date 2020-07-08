package com.example.blitz_github.dialoguebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.R;
import com.google.android.material.textfield.TextInputLayout;

import static com.facebook.FacebookSdk.getApplicationContext;

public class resumeHeadline extends AppCompatDialogFragment {

    private TextInputLayout resumeheadline;
    private String headline;
    private TextView showheadline;

    public resumeHeadline() {
    }
//getting text and pointer from  complete registration
    public resumeHeadline(String headline, TextView showheadline) {
        this.headline = headline;
        this.showheadline = showheadline;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        Log.v("reached here","");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_resume_headline, null);

        builder.setView(view)
                .setTitle("Enter Resume Headline")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String headlinetext= resumeheadline.getEditText().getText().toString().trim();

                        if(headlinetext.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Resume Headline is mandatory. Resume appear with headline to recruiters.",Toast.LENGTH_SHORT).show();
                        }
                        //for putting text in textview in completeregistration section
                        else
                        {
                            showheadline.setText(headlinetext);
                        }

                    }
                });
        //putting data in dialogbox
        resumeheadline=view.findViewById(R.id.headline_id);
        resumeheadline.getEditText().setText(headline);

        return builder.create();

    }

}
