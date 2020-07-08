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
import com.example.blitz_github.Account.login_register;
import com.example.blitz_github.Utils.registerHelper;

public class forgetpassword_dialogboxclass extends AppCompatDialogFragment {


    public interface Mylisterner
    {
        void resetPassword(String email);
    }

    private EditText editTextkeyemail;
    Mylisterner mylisterner;

    public void setupListener(Mylisterner listerner)
    {
        mylisterner = listerner;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.forgetpassword_dialogbox, null);
        builder.setView(view)
                .setTitle("Enter Email Address")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newemail= editTextkeyemail.getText().toString();
                        mylisterner.resetPassword(newemail);
                    }
                });

        editTextkeyemail=view.findViewById(R.id.forgetEmail);

        return builder.create();

    }
}
