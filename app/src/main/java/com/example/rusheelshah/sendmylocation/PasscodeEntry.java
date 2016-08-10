package com.example.rusheelshah.sendmylocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PasscodeEntry extends Activity {

    String userEntered;
    String userPin = "8888";

    final int PIN_LENGTH = 4;
    boolean keyPadLockedFlag = false;

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button buttonClear;
    TextView passwordInput;
    CheckBox checkBox;
    boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEntered = "";
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passcode_entry);
        passwordInput = (TextView) findViewById(R.id.password_input);
        checkBox = (CheckBox) findViewById(R.id.check_box);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    isChecked = true;
                }
                else{
                    isChecked = false;
                }
            }
        });

        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userEntered.compareTo("") == 0){

                }
                else{
                    passwordInput.setText(passwordInput.getText().toString().substring(0, passwordInput.getText().toString().length() - 1));
                    userEntered = userEntered.substring(0, userEntered.length() - 1);
                }
            }
        });
        View.OnClickListener pinButtonHandler = new View.OnClickListener() {
            public void onClick(View view) {

                if (keyPadLockedFlag == true) {
                    return;
                }

                Button pressedButton = (Button) view;

                if (userEntered.length() < PIN_LENGTH) {
                    userEntered = userEntered + pressedButton.getText();
                    Log.v("PinView", "User entered=" + userEntered);

                    if(isChecked){
                        passwordInput.setText(userEntered);
                    }

                    else{
                        //Update pin boxes
                        String temp = hidePasscode(passwordInput.getText().toString());
                        passwordInput.setText(temp + "*");
                    }

                    if (userEntered.length() == PIN_LENGTH) {
                        //Check if entered PIN is correct
                        if (userEntered.equals(userPin)) {
                            Log.v("PinView", "Correct PIN");
                            Intent intent = new Intent(PasscodeEntry.this, EnterContact.class);
                            startActivity(intent);
                        } else {
                            keyPadLockedFlag = true;
                            Log.v("PinView", "Wrong PIN");
                            new LockKeyPadOperation().execute("");
                        }
                    }
                } else {
                    //Roll over
                    passwordInput.setText("");

                    userEntered = "";
                    userEntered = userEntered + pressedButton.getText();
                    Log.v("PinView", "User entered=" + userEntered);
                }


            }
        };
        button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(pinButtonHandler);

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(pinButtonHandler);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(pinButtonHandler);


        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(pinButtonHandler);

        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(pinButtonHandler);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(pinButtonHandler);

        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(pinButtonHandler);

        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(pinButtonHandler);

        button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(pinButtonHandler);

        button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(pinButtonHandler);
    }

    private class LockKeyPadOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            //Roll over
            passwordInput.setText("");
            ;

            userEntered = "";

            keyPadLockedFlag = false;
        }

    }
    public String hidePasscode(String string){
        StringBuilder sb = new StringBuilder();
        for(char c : string.toCharArray()){
            sb.append("*");
        }
        return sb.toString();
    }
}
