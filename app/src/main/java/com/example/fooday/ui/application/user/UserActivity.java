package com.example.fooday.ui.application.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class UserActivity extends AppCompatActivity {
    Button orderNow;
    TextView todayMeal,breakfastMeal,lunchMeal,dinnerMeal,breakfast,lunch,dinner;
    DBHelper DB;
    DayOfWeek day;
    Date date;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent i = getIntent();
        String username = i.getStringExtra("username");

        initComponent();

        date = new Date();
        day = (DayOfWeek.of(date.getDay()));

        try {
            viewData(day.toString());

        }catch (Exception e){

            Toast.makeText(this, "Error loading meal", Toast.LENGTH_SHORT).show();
        }



        orderNow.setOnClickListener(v -> {
            try {
                DB.InsertOrder(breakfastMeal.getText().toString(),
                        date.getDay(),"Breakfast",username);
                DB.InsertOrder(lunchMeal.getText().toString(),
                        date.getDay(),"Lunch",username);
                DB.InsertOrder(dinnerMeal.getText().toString(),
                        date.getDay(),"Dinner",username);

                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void viewData(String day) {
        todayMeal.setText(day+"'s meal");
        breakfastMeal.setText(DB.getBreakFastByDay(date.getDay()));
        lunchMeal.setText(DB.getLunchByDay(date.getDay()));
        dinnerMeal.setText(DB.getDinnerByDay(date.getDay()));
    }

    private void initComponent() {

        orderNow = findViewById(R.id.order);
        todayMeal = findViewById(R.id.meals);
        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        breakfastMeal = findViewById(R.id.breakfastMeal);
        lunchMeal = findViewById(R.id.lunchMeal);
        dinnerMeal = findViewById(R.id.dinnerMeal);
        DB = new DBHelper(this);



    }
}