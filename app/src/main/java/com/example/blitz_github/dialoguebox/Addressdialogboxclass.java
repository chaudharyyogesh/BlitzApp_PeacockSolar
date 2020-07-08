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

public class Addressdialogboxclass extends AppCompatDialogFragment {
    private EditText fullAddress;
    private String addressstring;
    private TextView addresslist;

    public Addressdialogboxclass(String addressstring, TextView addresslist) {
        this.addressstring = addressstring;
        this.addresslist = addresslist;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.address_dialogbox, null);

        builder.setView(view)
                .setTitle("Enter Your Address")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here we will take the skills from edit text and push to database
                        String full_address= fullAddress.getText().toString();

                        //textview in completeregistration section
                        addresslist.setText(full_address);
                    }
                });

        fullAddress=view.findViewById(R.id.editfulladdress);
        fullAddress.setText(addressstring);

        return builder.create();

    }
}
