package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class ProfileCustomizationActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneInput;
    private Spinner genderSpinner;
    private Button dobButton;
    private Button saveButton;
    private Button backButton;
    private SharedPreferences sharedPreferences;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customization);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Initialize views
        firstNameInput = findViewById(R.id.edit_first_name_input);
        lastNameInput = findViewById(R.id.edit_last_name_input);
        phoneInput = findViewById(R.id.edit_phone_input);
        genderSpinner = findViewById(R.id.edit_gender_spinner);
        dobButton = findViewById(R.id.edit_dob_button);
        saveButton = findViewById(R.id.save_profile_button);
        backButton = findViewById(R.id.back_button);

        // Initialize date
        selectedDate = Calendar.getInstance();

        // Set up gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Load existing user information
        String firstName = sharedPreferences.getString("first_name", "Not set");
        String lastName = sharedPreferences.getString("last_name", "Not set");
        String phone = sharedPreferences.getString("phone", "Not set");
        String gender = sharedPreferences.getString("gender", "Not set");
        String dob = sharedPreferences.getString("dob", "Not set");

        firstNameInput.setText(firstName.equals("Not set") ? "" : firstName);
        lastNameInput.setText(lastName.equals("Not set") ? "" : lastName);
        phoneInput.setText(phone.equals("Not set") ? "" : phone);
        if (!gender.equals("Not set")) {
            int position = adapter.getPosition(gender);
            genderSpinner.setSelection(position);
        }
        dobButton.setText(dob.equals("Not set") ? "What is your date of birth?" : dob);

        // Date picker for DOB
        dobButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(year, month, dayOfMonth);
                        String dobText = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        dobButton.setText(dobText);
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String newFirstName = firstNameInput.getText().toString().trim();
            String newLastName = lastNameInput.getText().toString().trim();
            String newPhone = phoneInput.getText().toString().trim();
            String newGender = genderSpinner.getSelectedItem().toString();
            String newDob = dobButton.getText().toString();


            // Basic validation
            if (newFirstName.isEmpty()) {
                Toast.makeText(this, "First name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newLastName.isEmpty()) {
                Toast.makeText(this, "Last name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPhone.isEmpty() || !android.util.Patterns.PHONE.matcher(newPhone).matches()) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newDob.equals("What is your date of birth?")) {
                Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the updated information to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("first_name", newFirstName);
            editor.putString("last_name", newLastName);
            editor.putString("phone", newPhone);
            editor.putString("gender", newGender);
            editor.putString("dob", newDob);
            editor.apply();

            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

            // Finish the activity to return to ProfileFragment
            finish();
        });

        // Back button click listener
        backButton.setOnClickListener(v -> {
            // Finish the activity to return to ProfileFragment
            finish();
        });
    }
}