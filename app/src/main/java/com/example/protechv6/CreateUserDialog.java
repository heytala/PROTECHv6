package com.example.protechv6;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateUserDialog extends AppCompatDialogFragment {
    private TextView textViewUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_user, null);

        builder.setView(view)
                .setTitle("Create a New User")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        String password = editTextPassword.getText().toString();
//                        String passwordConfirm = editTextPasswordConfirm.getText().toString();
//
//                        System.out.println(password);
//                        System.out.println(passwordConfirm);
//
//
//
//                        if (password.equals(passwordConfirm)) {
//                            listener.applyTexts(password);
//                            System.out.println("***************Listener applied password");
//                            Toast.makeText
//                                    (getContext(),"User Created",Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText
//                                    (getContext(),"Passwords do not Match!",Toast.LENGTH_LONG).show();
//                        }

                    }
                });

        textViewUsername = view.findViewById(R.id.username);
        editTextPassword = view.findViewById(R.id.edit_password);
        editTextPasswordConfirm = view.findViewById(R.id.edit_passwordConfirm);

        return builder.create();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String password = editTextPassword.getText().toString();
                    String passwordConfirm = editTextPasswordConfirm.getText().toString();

                    System.out.println(password);
                    System.out.println(passwordConfirm);


                    if (password.equals(passwordConfirm) && !password.isEmpty()) {
                        listener.applyTexts(password);
                        System.out.println("***************Listener applied password");
                        d.dismiss();

                        Toast.makeText
                                (getContext(),"User Created",Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText
//                                (getContext(),"Passwords do not Match!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }


    public interface ExampleDialogListener {
        void applyTexts(String password);

    }


}