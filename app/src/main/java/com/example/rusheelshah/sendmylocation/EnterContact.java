package com.example.rusheelshah.sendmylocation;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class EnterContact extends AppCompatActivity implements OnMyLocationButtonClickListener, OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private GoogleMap mMap;
    public String latitude;         //latitude to be sent
    public String longitude;        //longitude to be sent
    public String recieveLat;       //latitude to be received
    public String recieveLong;      //longitude to be received
    public String senderNum;        //phone number of sender
    public String recieverNum = "";      //phone number of receiver
    public String phoneNum;         //phone number of sender
    //String query_num;
    LocationManager lm;
    LatLng myLocation;
    public Firebase mDatabase;
    public String refreshedToken;

    DBHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact);
        mDatabase.setAndroidContext(this);
        mDb = new DBHelper(this);
        mDatabase = new Firebase("https://send-my-location-6be19.firebaseio.com/");
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Query queryRef = mDatabase.orderByChild("recieverNumber");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> value = (Map<String, Object>) snapshot.getValue();
                    for(Map.Entry<String, Object> entry: value.entrySet()){
                        String key = entry.getKey();
                        Object o = entry.getValue();
                        if(key.compareTo("File Content") != 0){
                            HashMap<String, String> hMap = (HashMap<String, String>) entry.getValue();
                            for(HashMap.Entry<String, String> hEntry: hMap.entrySet()){
                                String hKey = hEntry.getKey();
                                String hValue = hEntry.getValue();
                                if(hKey.compareTo("recieverNumber") == 0){
                                    //System.out.println("reciever number = " + hValue);
                                    recieverNum = hValue;
                                    value.remove(entry);
                                }
                                else if(hKey.compareTo("latitude") == 0){
                                    //System.out.println("latitude = " + hValue);
                                    recieveLat = hValue;
                                }
                                else if(hKey.compareTo("longitude") == 0){
                                    //System.out.println("longitude = " + hValue);
                                    recieveLong = hValue;
                                }
                                else if(hKey.compareTo("senderNum") == 0){
                                    //System.out.println("sender number = " + hValue);
                                    senderNum = hValue;
                                }
                            }
                            if(phoneNum != null && !phoneNum.isEmpty()){
                                if(recieverNum.compareTo(phoneNum) == 0){
                                    double tempLat = Double.parseDouble(recieveLat);
                                    double tempLong = Double.parseDouble(recieveLong);
                                    LatLng recieveLocation = new LatLng(tempLat, tempLong);
                                    mMap.addMarker(new MarkerOptions().position(recieveLocation).title(senderNum + " is here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                }
                            }
                        }
                    }
                }


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
//                if(snapshot.exists()) {
//                    if (snapshot.toString().compareTo("File Content") != 0) {
//                        Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
//                        for (Map.Entry<String, Object> entry : value.entrySet()) {
//                            String key = entry.getKey();
//                            String val = entry.getValue().toString();
//                            if (key.compareTo("File Content") != 0) {
//                                if (key.compareTo("recieverNumber") == 0) {
//                                    //System.out.println("reciever number = " + hValue);
//                                    recieverNum = val;
//                                } else if (key.compareTo("latitude") == 0) {
//                                    //System.out.println("latitude = " + hValue);
//                                    recieveLat = val;
//                                } else if (key.compareTo("longitude") == 0) {
//                                    //System.out.println("longitude = " + hValue);
//                                    recieveLong = val;
//                                } else if (key.compareTo("senderNum") == 0) {
//                                    //System.out.println("sender number = " + hValue);
//                                    senderNum = val;
//                                }
//                                if (phoneNum != null && !phoneNum.isEmpty()) {
//                                    if (recieverNum.compareTo(phoneNum) == 0) {
//                                        double tempLat = Double.parseDouble(recieveLat);
//                                        double tempLong = Double.parseDouble(recieveLong);
//                                        LatLng recieveLocation = new LatLng(tempLat, tempLong);
//                                        mMap.addMarker(new MarkerOptions().position(recieveLocation).title(senderNum + " is here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                                    }
//                                }
//                            }
//                            if (key.compareTo("File Content") != 0) {
//                                HashMap<String, String> hMap = (HashMap<String, String>) entry.getValue();
//                                for (HashMap.Entry<String, String> hEntry : hMap.entrySet()) {
//                                    String hKey = hEntry.getKey();
//                                    String hValue = hEntry.getValue();
//                                    if (hKey.compareTo("recieverNumber") == 0) {
//                                        //System.out.println("reciever number = " + hValue);
//                                        recieverNum = hValue.toString();
//                                    } else if (hKey.compareTo("latitude") == 0) {
//                                        //System.out.println("latitude = " + hValue);
//                                        recieveLat = hValue.toString();
//                                    } else if (hKey.compareTo("longitude") == 0) {
//                                        //System.out.println("longitude = " + hValue);
//                                        recieveLong = hValue.toString();
//                                    } else if (hKey.compareTo("senderNum") == 0) {
//                                        //System.out.println("sender number = " + hValue);
//                                        senderNum = hValue.toString();
//                                    }
//                                }
//                                if (phoneNum != null && !phoneNum.isEmpty()) {
//                                    if (recieverNum.compareTo(phoneNum) == 0) {
//                                        double tempLat = Double.parseDouble(recieveLat);
//                                        double tempLong = Double.parseDouble(recieveLong);
//                                        LatLng recieveLocation = new LatLng(tempLat, tempLong);
//                                        mMap.addMarker(new MarkerOptions().position(recieveLocation).title(senderNum + " is here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//
//            }
//            @Override
//            public void onChildChanged(DataSnapshot snapshot, String previousChildName){
//                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
//                for (Map.Entry<String, Object> entry : value.entrySet()) {
//                    String key = entry.getKey();
//                    String val = entry.getValue().toString();
//                    if (key.compareTo("File Content") != 0) {
//                        if (key.compareTo("recieverNumber") == 0) {
//                            //System.out.println("reciever number = " + hValue);
//                            recieverNum = val;
//                        } else if (key.compareTo("latitude") == 0) {
//                            //System.out.println("latitude = " + hValue);
//                            recieveLat = val;
//                        } else if (key.compareTo("longitude") == 0) {
//                            //System.out.println("longitude = " + hValue);
//                            recieveLong = val;
//                        } else if (key.compareTo("senderNum") == 0) {
//                            //System.out.println("sender number = " + hValue);
//                            senderNum = val;
//                        }
//                        if (phoneNum != null && !phoneNum.isEmpty()) {
//                            if (recieverNum.compareTo(phoneNum) == 0) {
//                                double tempLat = Double.parseDouble(recieveLat);
//                                double tempLong = Double.parseDouble(recieveLong);
//                                LatLng recieveLocation = new LatLng(tempLat, tempLong);
//                                mMap.addMarker(new MarkerOptions().position(recieveLocation).title(senderNum + " is here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                            }
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onChildMoved(DataSnapshot snapshot, String previousChildName){
//                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
//                for (Map.Entry<String, Object> entry : value.entrySet()) {
//                    String key = entry.getKey();
//                    String val = entry.getValue().toString();
//                    if (key.compareTo("File Content") != 0) {
//                        if (key.compareTo("recieverNumber") == 0) {
//                            //System.out.println("reciever number = " + hValue);
//                            recieverNum = val;
//                        } else if (key.compareTo("latitude") == 0) {
//                            //System.out.println("latitude = " + hValue);
//                            recieveLat = val;
//                        } else if (key.compareTo("longitude") == 0) {
//                            //System.out.println("longitude = " + hValue);
//                            recieveLong = val;
//                        } else if (key.compareTo("senderNum") == 0) {
//                            //System.out.println("sender number = " + hValue);
//                            senderNum = val;
//                        }
//                        if (phoneNum != null && !phoneNum.isEmpty()) {
//                            if (recieverNum.compareTo(phoneNum) == 0) {
//                                double tempLat = Double.parseDouble(recieveLat);
//                                double tempLong = Double.parseDouble(recieveLong);
//                                LatLng recieveLocation = new LatLng(tempLat, tempLong);
//                                mMap.addMarker(new MarkerOptions().position(recieveLocation).title(senderNum + " is here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//                            }
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot snapshot){
//
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);
        Location myLocation = getLastKnownLocation();
        onLocationChanged(myLocation);

    }

    private Location getLastKnownLocation() {
        lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public void onSendButton(View view){
        EditText senderNum = (EditText) findViewById(R.id.sender_num);
        phoneNum = senderNum.getText().toString();
        mDb.insertContact("Me", phoneNum);
        //query_num = phoneNum;
        Toast.makeText(this, "Your number is" + " " + phoneNum, Toast.LENGTH_LONG).show();
        mDatabase.child("sender: " + phoneNum).child("senderNum").setValue(phoneNum);
        mDatabase.child("sender: " + phoneNum).child("latitude").setValue(latitude);
        mDatabase.child("sender: " + phoneNum).child("longitude").setValue(longitude);
        Intent intent = new Intent(this, GetContacts.class);
        intent.putExtra("PHONE_NUM", phoneNum);
        startActivity(intent);
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;
        double tempLat = Double.parseDouble(latitude);
        double tempLong = Double.parseDouble(longitude);
        myLocation = new LatLng(tempLat, tempLong   );
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Current Location: " + String.valueOf(latitude) + ", " + String.valueOf(longitude)));
        float zoom = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location){
        double tempLatitude = location.getLatitude();
        double tempLongitude = location.getLongitude();
        latitude = Double.toString(tempLatitude);
        longitude = Double.toString(tempLongitude);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void onSendFile(View view){
        Intent intent = new Intent(this, SendFileActivity.class);
        startActivity(intent);
    }
}