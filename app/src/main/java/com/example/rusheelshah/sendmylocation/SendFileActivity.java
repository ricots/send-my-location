package com.example.rusheelshah.sendmylocation;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SendFileActivity extends AppCompatActivity {

    //private static final String FILE_PATH = File.separator + "Users" + File.separator + "rusheelshah"+ File.separator + "Desktop" + File.separator + "test.txt";
    //private static final String FILE_DIR = File.separator + "Users" + File.separator + "rusheelshah"+ File.separator + "Desktop" + File.separator;

    EditText etFileData;
    TextView tvFileContents;
    TextView tvFilePath;
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "SendFileSample" + File.separator + "test.txt";
    private static final String FILE_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + "SendFileSample" + File.separator;
    public String data = "Sample Message ";
    public Firebase mDatabase;
    public String refreshedToken;
    File file = new File(FILE_PATH);
    String returnContent;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);
        mDatabase = new Firebase("https://send-my-location-6be19.firebaseio.com/");
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //EditText, Enter Data:
        //etFileData = (EditText) findViewById(R.id.et_add_io);


        tvFileContents = (TextView) findViewById(R.id.tv_io_cipher);
        tvFilePath = (TextView) findViewById(R.id.tv_file_path);
        createFile();
        writeToFile(file);
        readFromFile();
        returnContent = getStringFromFirebase();
    }

    private void createFile() {
        File dir = new File(FILE_DIR);
        File file = new File(FILE_PATH);
        tvFilePath.setText("FilePath:"+FILE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
            dir.setExecutable(true);
            dir.setReadable(true);
            dir.setWritable(true);
        }
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
                Toast.makeText(this, "created file:" +FILE_PATH + result, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                tvFileContents.setText(e.getMessage());
            }
        } else {
            Toast.makeText(this, "already   created file:" + Environment.getExternalStorageDirectory() +
                    File.separator + "SendFileSample" + File.separator + "test.txt", Toast.LENGTH_SHORT).show();
        }
    }
    public void writeToFile(File file){
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(data);
            writer.flush();
            writer.close();
            Toast.makeText(this, "added data to file: " + data, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tvFileContents.setText("Unable to write to file " + e.getMessage());
        }
    }
    public void readFromFile(){
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            readfileInput(fis);
        } catch (Exception e) {
            tvFileContents.setText("Unable to read from file " + e.getMessage());
        }
    }
    public void addFileOutputStreamLine(View view) {
        String data = etFileData.getText().toString();
        if (data.isEmpty()) {
            tvFileContents.setText("Please Enter some data");
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(FILE_PATH, true);
                fos.write(data.getBytes());
                fos.close();
                Toast.makeText(this, "added data to file:", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                tvFileContents.setText("Unable to write to file " + e.getMessage());
            }
        }
    }

    private void readfileInput(FileInputStream fis) throws IOException {
        int i;
        String str = "";
        while ((i = fis.read()) != -1) {
            // convert integer to character
            str += (char) i;
        }
        //tvFileContents.setText("READ:" + str);
        sendStringToFirebase(str);
        System.out.println("Read: " + str);
        fis.close();
    }
    public void sendStringToFirebase(String string){
        mDatabase.child("File Content").setValue(string);

    }
    public String getStringFromFirebase(){
        Query queryRef = mDatabase.orderByChild("File Content");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    content = (String) snapshot.child("File Content").getValue();
                    tvFileContents.setText("File Contents: " + content);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return content;

    }



    public void createTempFile(View view) {
        try {
            File file = File.createTempFile("tmp", ".txt", new File(FILE_DIR));
            if (file.exists())
                tvFileContents.setText("TempFile created at: " + file.getAbsolutePath());
            else
                tvFileContents.setText("unable to create temp file");
            file.deleteOnExit();
        } catch (Exception e) {
            tvFileContents.setText("Unable to create from file " + e.getMessage());
        }
    }

    public void openFileOutput(View view) {
        String data = etFileData.getText().toString();
        if (data.isEmpty()) {
            tvFileContents.setText("Please Enter some data");
        } else {
            try {
                FileOutputStream fos = openFileOutput("AirwatchTest.txt", Context.MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
                tvFileContents.setText("File AirwatchTest.txt is now located in internal memory at "+getFilesDir().getAbsolutePath());
                Toast.makeText(this, "added data to file", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                tvFileContents.setText("Unable to write to file " + e.getMessage());
            }
        }
    }

    public void readOpenFileInput(View view) {
        try {
            FileInputStream fis = openFileInput("AirwatchTest.txt");
            readfileInput(fis);
            tvFileContents.append("\nFile AirwatchTest.txt is now located in internal memory at"+getFilesDir().getAbsolutePath());
        } catch (Exception e) {
            tvFileContents.setText("Unable to read from file " + e.getMessage());
        }
    }
}
