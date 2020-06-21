package com.jakubsiwiec.smsmessenger;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    String TAG = "Second Fragment";

    private DataBaseHelper dataBaseHelper;

    // Edit text fields
    private EditText editTextMessage;
    private EditText editTextPhoneNumberMsg;

    // Values stored in edit text
    private String message;
    private String phoneNumber;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextPhoneNumberMsg = (EditText) view.findViewById(R.id.editTextPhoneMsg);
        editTextMessage = (EditText) view.findViewById(R.id.editTextMessage);


        view.findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                This function sends SMS message
                At first it is checked, if phone number is entered
                Then the message is sent, and the results are written to DB
                 */

                phoneNumber = editTextPhoneNumberMsg.getText().toString();
                if (!phoneNumber.matches("")){
                    message = editTextMessage.getText().toString();

                    Log.i(TAG, "Phone number: " + phoneNumber + "\nMessage: " + message);
                    editTextMessage.getText().clear();
                }
                else {
                    Toast.makeText(getContext(), "Enter phone number before sending the message", Toast.LENGTH_LONG).show();
                }
            }
        });


        view.findViewById(R.id.buttonContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_contactsFragment);
            }
        });
    }
}