package com.jakubsiwiec.smsmessenger;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private static final String TAG = "ListDataFragment";
    private DataBaseHelper dataBaseHelper;
    private ListView listViewContacts;


    private void populateListView(){

        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getContacts();
        ArrayList<String> listData = new ArrayList<>();
        int i = 1;
        while(data.moveToNext()){

            // Get name and phone number
            String contactName = data.getString(1);
            String contactPhoneNumber = data.getString(2);

            listData.add(contactName + "\n" + contactPhoneNumber);
            Log.d(TAG, data.getString(1));
            i++;
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        listViewContacts.setAdapter(adapter);

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

    }
}