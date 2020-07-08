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

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.R;

public class Languagedialogboxclass extends AppCompatDialogFragment {
    private EditText language;
    private String languagestring;
    private TextView languagelist;

    public Languagedialogboxclass(String languagestring, TextView languagelist) {
        this.languagestring = languagestring;
        this.languagelist = languagelist;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.language_dialogbox, null);

        builder.setView(view)
                .setTitle("Enter Language")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                         String language_new= language.getText().toString();

                        //textview in completeregistration section
                        languagelist.setText(language_new);
                    }
                });

        language=view.findViewById(R.id.editlanguage);
        language.setText(languagestring);

        return builder.create();

    }

}
