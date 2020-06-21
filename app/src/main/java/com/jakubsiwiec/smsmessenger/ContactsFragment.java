package com.jakubsiwiec.smsmessenger;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private static final String TAG = "ListDataFragment";
    public static final String cpn = "";
    private DataBaseHelper dataBaseHelper;
    private ListView listViewContacts;


    private void populateListView(){

        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getContacts();
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String>  maintitle = new ArrayList<>();
        ArrayList<String>  subtitle = new ArrayList<>();
        ArrayList<Integer>  imgid= new ArrayList<>();
        int i = 1;
        while(data.moveToNext()){

            // Get name and phone number
            String contactName = data.getString(1);
            String contactPhoneNumber = data.getString(2);

            maintitle.add(contactName);
            subtitle.add(contactPhoneNumber);
            imgid.add(R.drawable.ic_baseline_account_circle_24);

            Log.d(TAG, data.getString(1));
            i++;
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        CustomContactListAdapter adapter=new CustomContactListAdapter(getContext(), maintitle, subtitle,imgid);
        listViewContacts.setAdapter(adapter);

    }

//    private void editCcntact(String name){
//
//
//
//    }


    private AlertDialog deleteEditDialog(Object o){

        Log.d(TAG, "Contact being edited");

        final String contactName = o.toString();


        //Access phone number
        Cursor contactRow = dataBaseHelper.getContacts(contactName);
        contactRow.moveToNext();
        String oldContactPhoneNumber = contactRow.getString(2);

        //Create dialog
        AlertDialog.Builder editDeleteContactsDialogBuilder = new AlertDialog.Builder(getActivity());
        editDeleteContactsDialogBuilder.setTitle(contactName + " - Edit Contact");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_custom_view,null);

        // Specify alert dialog is not cancelable/not ignorable
        editDeleteContactsDialogBuilder.setCancelable(false);

        // Set the custom layout as alert dialog view
        editDeleteContactsDialogBuilder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        final EditText editTextNameEditContact = (EditText) dialogView.findViewById(R.id.editTextNameContact);
        final EditText editTextPhoneEditContact = (EditText) dialogView.findViewById(R.id.editTextPhoneContact);

        editTextNameEditContact.setText(contactName);
        editTextPhoneEditContact.setText(oldContactPhoneNumber);

        editDeleteContactsDialogBuilder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked EDIT CONTACT button
                //Check if contact with new entered name exists
                //if not, then replace the old name and phone with provided
                //When dialog shows up, the edit text fields should contain old name and phone

                String newName = editTextNameEditContact.getText().toString();
                String newContactPhoneNumber = editTextPhoneEditContact.getText().toString();

                // CHeck if the newly selected name isn't occupied
                if (dataBaseHelper.checkIfContactExists(newName)){
                    Toast.makeText(getContext(), "Contact already exists", Toast.LENGTH_LONG);
                }
                // Update DB if not, and populate list view to show changes immediately
                else{
                    dataBaseHelper.EditContact(contactName, newName, newContactPhoneNumber);
                    populateListView();
                }


            }
        });



        editDeleteContactsDialogBuilder.setNegativeButton("DELETE CONTACT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dataBaseHelper.deleteContact(contactName);
                populateListView();
            }
        });


        return editDeleteContactsDialogBuilder.create();
    }


    public ContactsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewContacts = (ListView) view.findViewById(R.id.listViewContacts);
        dataBaseHelper = new DataBaseHelper(getContext());

        populateListView();

        listViewContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                Object o = listViewContacts.getItemAtPosition(position);

                Log.i(TAG, "Item of position " + position + " and l " + l + " held");

                AlertDialog deleteDialog = deleteEditDialog(o);
                deleteDialog.show();
                return true;
            }

        });

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listViewContacts.getItemAtPosition(position);

                Bundle bundle = new Bundle();

                Cursor contactRow = dataBaseHelper.getContacts(o.toString());
                contactRow.moveToNext();
                String contactPhoneNumber = contactRow.getString(2);

                bundle.putString("contactPhoneNumber", contactPhoneNumber);

                NavHostFragment.findNavController(ContactsFragment.this)
                        .navigate(R.id.action_contactsFragment_to_SecondFragment, bundle);



                Log.i(TAG, "Item of position " + position + " clicked");

            }
        });
    }
}