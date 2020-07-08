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

import com.example.blitz_github.Account.Complete_Registration;
import com.example.blitz_github.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class phonenumber_dialogboxclass extends AppCompatDialogFragment {

    private EditText editTextkeynumber;
    private String phonenumberstring;
    private TextView phonenumber;
    String MobilePattern="[0-9]{10}";


    public  phonenumber_dialogboxclass(String phonenumberstring,TextView phonenumber){
        this.phonenumberstring=phonenumberstring;
        this.phonenumber=phonenumber;

    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.phonenumber_dialogbox, null);


        builder.setView(view)
                .setTitle("Enter Phone Number")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the phone number from edit text and push to database
                        String newnumber = editTextkeynumber.getText().toString();
                        //this will update the textview in completeregistration section
                        if (!newnumber.matches(MobilePattern) || newnumber.length() != 10) {
                            //This error is not working.
                            editTextkeynumber.setError("The Phone number is Invalid");
                        }
                        else{
                            phonenumber.setText(newnumber);
                        }

                    }
                });

        editTextkeynumber = view.findViewById(R.id.editphonenumber);
        editTextkeynumber.setText(phonenumberstring);

        return builder.create();

    }
}
