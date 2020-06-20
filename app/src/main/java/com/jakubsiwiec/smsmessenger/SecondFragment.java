package com.jakubsiwiec.smsmessenger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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


        editTextPhoneNumberMsg = (EditText) view.findViewById(R.id.editTextPhoneMsg);
        editTextMessage = (EditText) view.findViewById(R.id.editTextMessage);


        view.findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = editTextPhoneNumberMsg.getText().toString();
                message = editTextMessage.getText().toString();

                Log.i(TAG, "Phone number: " + phoneNumber + "\nMessage: " + message);
                editTextMessage.getText().clear();
            }
        });


        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}