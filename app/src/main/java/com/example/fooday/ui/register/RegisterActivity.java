package com.example.fooday.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooday.R;
import com.example.fooday.ui.application.user.UserActivity;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    Button logInBtn, RegisterBtn;


    public static void alertBox(String data, Context context) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView = findViewById(R.id.imageView);
        logInBtn = findViewById(R.id.login_btn);
        RegisterBtn = findViewById(R.id.signup_btn);


        logInBtn.setOnClickListener(v -> {
            replaceFragment(new LogInFragment());

        });

        RegisterBtn.setOnClickListener(v -> {
            replaceFragment(new SignUpFragment());
        });


    }

    private void replaceFragment(Fragment fragment) {
        imageView.setVisibility(android.view.View.GONE);
        logInBtn.setVisibility(android.view.View.GONE);
        RegisterBtn.setVisibility(android.view.View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentFrames, fragment);
        fragmentTransaction.commit();
    }
}