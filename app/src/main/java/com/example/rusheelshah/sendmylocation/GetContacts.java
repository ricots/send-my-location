package com.example.rusheelshah.sendmylocation;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.List;

public class GetContacts extends ListActivity {

    ListView listv;
    String[] names;
    Contact[] contacts;
    String[] numbers;
    public static ListActivity instance;
    public Object number;
    public Object name;
    ContactsProvider provider;
    ListAdapter la;
    ListAdapter nameList;
    private DatabaseReference mDatabase;
    public String phoneNum;

    private DBHelper mDb;
    int count;
    List<Contact> listc;

    private static final String FILE_PATH = File.separator + "Users" + File.separator + "rusheelshah" + "Desktop" + File.separator + "test.txt";
    private static final String FILE_DIR = "/Users/rusheelshah/Desktop/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = new DBHelper(this);

        Firebase.setAndroidContext(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_get_contacts);

        listv = getListView();
        listv.setClickable(true);
        instance = this;
        provider = new ContactsProvider();

        listc = fetchContacts();
        contacts = new Contact[listc.size()];
        names = new String[listc.size()];
        numbers = new String[listc.size()];

        for(int i = 0; i < listc.size(); i++){
            count++;
            Contact contactInstance = listc.get(i);
            contacts[i] = contactInstance;
            names[i] = contactInstance.getName();
            numbers[i] = contactInstance.getNum();
            mDb.insertContact(names[i], numbers[i]);
        }
        //System.out.println(mDb.getAllContacts());
        System.out.println(count);
        System.out.println(listc.size());

        la = new ArrayAdapter<Contact>(this, R.layout.simplerow, contacts);
        nameList = new ArrayAdapter<String>(this, R.layout.simplerow, names);
        listv.setAdapter(nameList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        Contact contact = (Contact) la.getItem(position);
        number = contact.getNum();
        name = contact.getName();
        String numString = number.toString();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNum = extras.getString("PHONE_NUM");
        }
        mDatabase.child("sender: " + phoneNum).child("recieverNumber").setValue(numString);
        String test = name.toString();
        Toast.makeText(this, "You're sending to " + test, Toast.LENGTH_LONG).show();
        System.out.println(mDb.getContact("Rusheel Shah"));
        mDb.deleteTable();
        finish();
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    public List<Contact> fetchContacts() {
        return provider.getContacts();
    }
}
