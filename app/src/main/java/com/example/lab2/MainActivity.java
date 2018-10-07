package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class MainActivity extends AppCompatActivity {

    private EditText fname;
    private EditText lname;
    private EditText phone;
    private EditText email;
    private EditText pass;
    private EditText cpass;
    private int id = 1;
    private Button submit, viewlist;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        updateUI();
    }

    /**
     * Called when the user taps the Send button
     */
    private void updateUI() {
        fname = findViewById(R.id.firstName);
        lname = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        cpass = findViewById(R.id.confirmPassword);
        submit = findViewById(R.id.submit);
        viewlist = findViewById(R.id.viewlist);

        String regexPhone = "[0-9]{10}";
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(MainActivity.this, R.id.firstName, "[a-zA-Z\\s]+", R.string.err_fname);
        awesomeValidation.addValidation(MainActivity.this, R.id.lastName, "[a-zA-Z\\s]+", R.string.err_lname);
        awesomeValidation.addValidation(MainActivity.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        awesomeValidation.addValidation(MainActivity.this, R.id.phone, regexPhone, R.string.err_phone);
        awesomeValidation.addValidation(MainActivity.this, R.id.password, regexPassword, R.string.err_pass);
        awesomeValidation.addValidation(MainActivity.this, R.id.confirmPassword, R.id.password, R.string.err_cpass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    saveToPreferences();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityChangeIntent = new Intent(MainActivity.this, UserDataActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
    }

    private void saveToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("userS", Context.MODE_PRIVATE);
        id = sharedPreferences.getAll().size()/4 + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name" + id, fname.getText().toString());
        editor.putString("surname" + id, lname.getText().toString());
        editor.putString("email" + id, email.getText().toString());
        editor.putString("phone" + id, phone.getText().toString());
        editor.apply();
    }
}
