package com.example.rusheelshah.sendmylocation;

/**
 * Created by rusheelshah on 6/1/16.
 */


public class Contact {
    public int id;
    public String name;
    public String phone;
    public String email;
    public String uriString;
    public Contact[] allNames;


    public Contact[] getAllNames(Contact[] contacts){
        for(int i = 0; i < contacts.length; i++){
            allNames[i] = contacts[i];
        }
        return allNames;
    }

    public String getNum(){
        return this.phone;
    }

    public String getName(){
        return this.name;
    }

}
