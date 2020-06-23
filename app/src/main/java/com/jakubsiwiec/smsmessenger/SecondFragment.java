package com.jakubsiwiec.smsmessenger;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    String TAG = "Second Fragment";

    private DataBaseHelper dataBaseHelper;

    private ListView listViewMessages;

    // Edit text fields
    private EditText editTextMessage;
    private EditText editTextPhoneNumberMsg;

    // Values stored in edit text
    private String message;
    private String phoneNumber = "";

    private int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;


    private void populateListView(String contactPhone){

        /*
        In this function messages list view will be populated
        using two text views: title - in which it will be shown if message was sent or received
        and subtitle, which is messages' content
        messages will be shown in order from the oldest at the bottom
         */
        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getContactMessages(phoneNumber);
        ArrayList<String> maintitle = new ArrayList<>();
        ArrayList<String>  subtitle = new ArrayList<>();
        String title;
        String messageToShow;
        int i = 1;
        while(data.moveToNext()){

            // Get name and phone number
            String contactName = data.getString(1); //Maybe show it later somewhere
            String lastMessage = data.getString(2);
            String dateTime = data.getString(4);

            String name = dataBaseHelper.getNameIfNumberExists(contactPhone);

            // Show if message was sent or received
            if (data.getInt(3) > 0){
                title = "Sent: ";
            }
            else{
                title = "Received: ";
            }

            title = title + dateTime;

            // If message is to long, trim it to fit in one line
            if (lastMessage.length() < 35){
                messageToShow = lastMessage;
            }
            else{
                messageToShow = lastMessage.substring(0, 35) + "...";
            }


            maintitle.add(title);
            subtitle.add(messageToShow);

            Log.d(TAG, data.getString(1));
            i++;
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        CustomMessagesListAdapter adapter = new CustomMessagesListAdapter(getContext(), maintitle, subtitle);
        listViewMessages.setAdapter(adapter);

    }


    //database insertion
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSentMessage(String phoneReceiver, String content){
        /*
        Save the message to the database
         */
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Toast.makeText(getContext(), "\nsent from: " + phoneReceiver + "\nContent: " + content, Toast.LENGTH_LONG).show();
        boolean insertData = dataBaseHelper.addMessage(phoneReceiver, content, timestamp, true); // use bool later if necessary
    }


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

        dataBaseHelper = new DataBaseHelper(getContext());

        editTextPhoneNumberMsg = (EditText) view.findViewById(R.id.editTextPhoneMsg);
        editTextPhoneNumberMsg.setText(phoneNumber);

        editTextMessage = (EditText) view.findViewById(R.id.editTextMessage);

        listViewMessages = (ListView) view.findViewById(R.id.listViewMessages);
        populateListView(phoneNumber);


        view.findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                    else {

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, ((MainActivity) getActivity()).sentPI, ((MainActivity) getActivity()).deliveredPI);

                        addSentMessage(phoneNumber, message);

                        Log.i(TAG, "Phone number: " + phoneNumber + "\nMessage: " + message);
                        editTextMessage.getText().clear();
                    }
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