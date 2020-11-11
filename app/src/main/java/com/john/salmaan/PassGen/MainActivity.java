package com.john.salmaan.PassGen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity /*implements SensorEventListener*/ {

    public boolean isChanging = false;

    private SensorManager manager;
    private float Accel;
    private float AccelCurrent;
    private float AccelLast;
    private boolean genAgain;
    private boolean isShaked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.generate);
        SeekBar seekBar = findViewById(R.id.lenseekbar);
        TextView password = findViewById(R.id.copy);
        EditText lenPass = findViewById(R.id.passsize);

        button.setOnClickListener(v -> {
            genPass();
        });

        password.setOnClickListener(v -> {
            copytoclipboard();
        });

        Toolbar toolbar = findViewById(R.id.custtoolbar);
        setSupportActionBar(toolbar);

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(manager).registerListener(sensorListener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Accel = 10f;
        AccelCurrent = SensorManager.GRAVITY_EARTH;
        AccelLast = SensorManager.GRAVITY_EARTH;

        genAgain = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isChanging) {
                    EditText lenPass = findViewById(R.id.passsize);
                    lenPass.setText(String.valueOf(seekBar.getProgress()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() < 10 && seekBar.getProgress() > 0) {
                    Toast.makeText(getApplicationContext(), "Weak password", Toast.LENGTH_LONG).show();
                }
                else if (seekBar.getProgress() >= 10 && seekBar.getProgress() <= 15) {
                    Toast.makeText(getApplicationContext(), "Medium password", Toast.LENGTH_LONG).show();
                }
                else if (seekBar.getProgress() > 15) {
                    Toast.makeText(getApplicationContext(), "Strong password", Toast.LENGTH_LONG).show();
                }

                isChanging = false;
            }
        });

        lenPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().equals("")) {
                    int length = Integer.parseInt(s.toString());

                    if (length <= 25) {
                        seekBar.setProgress(length);

                        if (!isChanging) {
                            if (length < 10 && length > 0) {
                                Toast.makeText(getApplicationContext(), "Weak password", Toast.LENGTH_LONG).show();
                            } else if (length >= 10 && length <= 15) {
                                Toast.makeText(getApplicationContext(), "Medium password", Toast.LENGTH_LONG).show();
                            } else if (length > 15) {
                                Toast.makeText(getApplicationContext(), "Strong password", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Password length limit is: 25", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // Generate password via button

    private void genPass() {
        TextView passShow = findViewById(R.id.showPw);
        EditText lenPass = findViewById(R.id.passsize);
        int size = Integer.parseInt(lenPass.getText().toString());

        CheckBox capital = findViewById(R.id.capitalandlower);
        CheckBox numbers = findViewById(R.id.number);
        CheckBox special = findViewById(R.id.special);

        boolean boolCapitalandLower = capital.isChecked();
        boolean boolNumber = numbers.isChecked();
        boolean boolSpecial = special.isChecked();

        ArrayList<String> pass_array = new ArrayList<>();
        String strCharacters = "abcdefghijklmnopqrstuvwxyz";
        String strCapCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String strNumbers = "0123456789";
        String strSpecChar = "!#$%&()*+,-./:;<=>?@[]^_{|}~";

        if (size >= 8 || genAgain) {

            for (int i = 0; i < size; i++) {

                if (boolSpecial && boolCapitalandLower && boolNumber) {
                    String passChars = strNumbers + strSpecChar + strCapCharacters + strCharacters;
                    int lenPassChar = passChars.length();

                    Random rd = new Random();
                    String random = String.valueOf(passChars.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolSpecial && boolCapitalandLower) {
                    String passChars = strSpecChar + strCharacters + strCapCharacters;
                    int lenPassChar = passChars.length();

                    Random rd = new Random();
                    String random = String.valueOf(passChars.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolSpecial && boolNumber) {
                    String passChars = strSpecChar + strNumbers;
                    int lenPassChar = passChars.length();

                    Random rd = new Random();
                    String random = String.valueOf(passChars.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolNumber && boolCapitalandLower) {
                    String passChars = strCapCharacters + strCharacters + strNumbers;
                    int lenPassChar = passChars.length();

                    Random rd = new Random();
                    String random = String.valueOf(passChars.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolNumber) {
                    int lenPassChar = strNumbers.length();

                    Random rd = new Random();
                    String random = String.valueOf(strNumbers.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolSpecial) {
                    int lenPassChar = strSpecChar.length();

                    Random rd = new Random();
                    String random = String.valueOf(strSpecChar.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else if (boolCapitalandLower) {
                    String passChars = strCapCharacters + strCharacters;
                    int lenPassChar = passChars.length();

                    Random rd = new Random();
                    String random = String.valueOf(passChars.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                } else {
                    int lenPassChar = strCharacters.length();

                    Random rd = new Random();
                    String random = String.valueOf(strCharacters.charAt(rd.nextInt(lenPassChar)));
                    pass_array.add(random);
                }
            }

            String gendpassword = TextUtils.join("", pass_array);
            passShow.setText(gendpassword);
        }
        else if (size == 0) {
            Toast.makeText(getApplicationContext(), "You can't make a 0 length password!!", Toast.LENGTH_LONG).show();
        }
        else {
            genAgain = true;
            Context context = getApplicationContext();
            CharSequence text = "Length too short. If you still want to generate, press again.";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run(){
                    genAgain = false;
                }
            }, 5000);
        }
    }

    // Generate Password via shaking

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            AccelLast = AccelCurrent;
            AccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float d = AccelCurrent - AccelLast;
            Accel = Accel * 0.9f + d;

            if (!isShaked) {
                if (Accel > 17) {
                    genPass();
                    isShaked = true;
                    Toast.makeText(getApplicationContext(), "Password Generated", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isShaked = false;
                        }
                    }, 2000);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };



    @Override
    protected void onPause() {
        manager.unregisterListener(sensorListener);
        super.onPause();
    }
    @Override
    protected void onResume() {
        manager.registerListener(sensorListener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    // Copy to clipboard
    private void copytoclipboard() {
        TextView password = findViewById(R.id.showPw);
        if (password.getText() != "") {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cliptext = ClipData.newPlainText("Password copied", password.getText());
            clipboard.setPrimaryClip(cliptext);
            Toast.makeText(getApplicationContext(), "Password copied", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "You haven't generated a password yet", Toast.LENGTH_LONG).show();
        }
    }
}
