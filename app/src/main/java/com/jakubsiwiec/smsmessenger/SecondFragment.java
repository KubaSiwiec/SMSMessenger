package com.jakubsiwiec.smsmessenger;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private String phoneNumber = "";

    private int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    private String SENT = "SMS SENT";
    private String DELIVERED = "SMS DELIVERED";
    private PendingIntent sentPI, deliveredPI;
    private BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Receive phone number from contact list
        // If it has been chosen before
        Bundle args = getArguments();

        if (args != null){
            phoneNumber = args.getString("contactPhoneNumber");
            Log.i(TAG, "Received phone number from contact list: " + phoneNumber);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextPhoneNumberMsg = (EditText) view.findViewById(R.id.editTextPhoneMsg);
        editTextPhoneNumberMsg.setText(phoneNumber);

        editTextMessage = (EditText) view.findViewById(R.id.editTextMessage);

        sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(DELIVERED), 0)


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

                    // Request permissions if they are not granted
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);



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