package com.example.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView firstNameView;
    private TextView lastNameView;
    private TextView phoneView;
    private TextView genderView;
    private TextView dobView;
    private Switch darkModeSwitch;
    private ImageView profilePicture;
    private Button updateProfileButton;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);

        // Initialize views
        firstNameView = view.findViewById(R.id.first_name_display);
        lastNameView = view.findViewById(R.id.last_name_display);
        phoneView = view.findViewById(R.id.phone_display);
        genderView = view.findViewById(R.id.gender_display);
        dobView = view.findViewById(R.id.dob_display);
        darkModeSwitch = view.findViewById(R.id.dark_mode_switch);
        profilePicture = view.findViewById(R.id.profile_picture);
        updateProfileButton = view.findViewById(R.id.update_profile_button);

        // Load and display user information
        loadUserInfo();

        // Apply the saved dark mode preference
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkMode);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("dark_mode", isChecked);
                editor.apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        // Update Profile button click listener
        updateProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileCustomizationActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserInfo() {
        String firstName = sharedPreferences.getString("first_name", "Not set");
        String lastName = sharedPreferences.getString("last_name", "Not set");
        String phone = sharedPreferences.getString("phone", "Not set");
        String gender = sharedPreferences.getString("gender", "Not set");
        String dob = sharedPreferences.getString("dob", "Not set");
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);

        firstNameView.setText("First Name: " + firstName);
        lastNameView.setText("Last Name: " + lastName);
        phoneView.setText("Phone: " + phone);
        genderView.setText("Gender: " + gender);
        dobView.setText("Date of Birth: " + dob);
        darkModeSwitch.setChecked(isDarkMode);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload user info when returning to this fragment
        loadUserInfo();

        // Ensure the switch reflects the current state
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkMode);
    }
}