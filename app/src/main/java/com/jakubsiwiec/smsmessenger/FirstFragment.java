package com.jakubsiwiec.smsmessenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    String TAG = "First fragment";
    private DataBaseHelper dataBaseHelper;

    private ListView listViewChats;


    private void populateListView(){

        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getLastMessageForEachContact();
        ArrayList<String> maintitle = new ArrayList<>();
        ArrayList<String>  subtitle = new ArrayList<>();
        ArrayList<Integer>  imgid= new ArrayList<>();
        String title;
        String messageToShow;
        int i = 1;
        while(data.moveToNext()){

            // Get name and phone number
//            String contactName = data.getString(1);
            String contactPhone = data.getString(1);    // Placeholder it is
            String lastMessage = data.getString(2);

            String name = dataBaseHelper.getNameIfNumberExists(contactPhone);
            if (name != "") {title = contactPhone + " (" + name + ")";} else {title = contactPhone;}

            // Show if message was sent or received
            if (data.getInt(3) > 0){
                title = "To: " + title;
            }
            else{
                title = "From: " + title;
            }

            // If message is to long, trim it to fit in one line
            if (lastMessage.length() < 35){
                messageToShow = lastMessage;
            }
            else{
                messageToShow = lastMessage.substring(0, 35) + "...";
            }
//            String dateTime = data.getString(4);

            maintitle.add(title);
            subtitle.add(messageToShow);
            imgid.add(R.drawable.ic_baseline_account_circle_24);

            Log.d(TAG, data.getString(1));
            i++;
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        CustomContactListAdapter adapter = new CustomContactListAdapter(getContext(), maintitle, subtitle,imgid);
        listViewChats.setAdapter(adapter);

    }

    //IF LIST VIEW IS EMPTY,
    //CREATE SOME FANCY NOTE THAT INFORMS USER THAT
    //THEY HAVE NO CHATS YET


    //database insertion
    public void addContact(String name, String phone){
        /*
        Check if contact with the entered name exists
        If not, add it to database
        If it existed before, inform the user
         */
        if(dataBaseHelper.checkIfContactExists(name)){
            Toast.makeText(getContext(), "Contact already exists", Toast.LENGTH_LONG).show();
        }
        else{
            boolean insertData = dataBaseHelper.addContact(name, phone);
            if(insertData){
                Toast.makeText(getContext(), "Inserted correctly", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "WROOOOONG! F", Toast.LENGTH_LONG).show();
            }
        }
    }


    private AlertDialog addContactDialog(){
        /*
        Show dialog which provides the interface of contact addition
        Dialog is customized - it consists of two text fields - name and phone number
        And two buttons - Add contact and cancel
         */

        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Contact");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_custom_view,null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        final EditText editTextNameAddContact = (EditText) dialogView.findViewById(R.id.editTextNameContact);
        final EditText editTextPhoneAddContact = (EditText) dialogView.findViewById(R.id.editTextPhoneContact);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = editTextNameAddContact.getText().toString();
                String phone = editTextPhoneAddContact.getText().toString();

                Log.i(TAG, "Adding " + name + ", " + phone);
                if (name.matches("") | phone.matches("")){
                    Toast.makeText(getContext(), "Contact not added, enter name and phone number", Toast.LENGTH_LONG).show();
                }
                else{
                    addContact(name, phone);
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        });

        // Create the alert dialog
        return builder.create();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Call DBhelper constructor
        Log.i(TAG, "Calling DBH constructor");
        dataBaseHelper = new DataBaseHelper(getContext());
        listViewChats = (ListView) view.findViewById(R.id.listViewChatss);

        // Prepare some security checking if table MESSAGES exists
        populateListView();


        view.findViewById(R.id.button_write_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        view.findViewById(R.id.button_add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Show the alert dialod which makes user able to add contact
                 */
                AlertDialog deleteDialog = addContactDialog();
                deleteDialog.show();
            }
        });


        listViewChats.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listViewChats.getItemAtPosition(position);

                String title = o.toString();

                // Retrieve the phone number from title
                int lastDigitIndex;
                int firstDigitIndex = title.indexOf(":") + 2;
                if (title.indexOf("(") == -1){
                    lastDigitIndex = title.lastIndexOf(title);
                }
                else{
                    lastDigitIndex = title.indexOf("(") - 1;
                }
                String contactPhoneNumber = title.substring(firstDigitIndex, lastDigitIndex);

                Bundle bundle = new Bundle();

                bundle.putString("contactPhoneNumber", contactPhoneNumber);

                Toast.makeText(getContext(), contactPhoneNumber, Toast.LENGTH_LONG).show();

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);



                Log.i(TAG, "Item of position " + position + " clicked");

            }
        });

    }
}