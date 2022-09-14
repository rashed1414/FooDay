package com.example.fooday.ui.application.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    Button orderNow;
    TextView todayMeal;
    AutoCompleteTextView appetizer,mainCourse,desert;
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

        List<String> appe = DB.GetFoodDayByType("Appetizer",date.getDay());
        List<String> main = DB.GetFoodDayByType("Main Course",date.getDay());
        List<String> des = DB.GetFoodDayByType("Desert",date.getDay());

        appetizer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appe));
        mainCourse.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, main));
        desert.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, des));

        appetizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                appetizer.showDropDown();
            }
        });

        mainCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                mainCourse.showDropDown();
            }
        });

        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                desert.showDropDown();
            }
        });




        try {
            viewData(day.toString());

        }catch (Exception e){

            Toast.makeText(this, "No Meals For Today Yet", Toast.LENGTH_SHORT).show();
        }



        orderNow.setOnClickListener(v -> {
            try {
                if (appetizer.getText().toString().isEmpty() || mainCourse.getText().toString().isEmpty() || desert.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Select Your Order", Toast.LENGTH_SHORT).show();
                } else {
                    DB.InsertOrder(appetizer.getText().toString(),
                            date.getDay(),"Appetizer",username);
                    DB.InsertOrder(mainCourse.getText().toString(),
                            date.getDay(),"Main Course",username);
                    DB.InsertOrder(desert.getText().toString(),
                            date.getDay(),"Desert",username);

                    Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void viewData(String day) {
        todayMeal.setText(day+"'s meal");
        //breakfastMeal.setText(DB.getBreakFastByDay(date.getDay()));
        //lunchMeal.setText(DB.getLunchByDay(date.getDay()));
        //dinnerMeal.setText(DB.getDinnerByDay(date.getDay()));
    }

    private void initComponent() {

        orderNow = findViewById(R.id.order);
        todayMeal = findViewById(R.id.meals);
        appetizer = findViewById(R.id.appetizer);
        mainCourse = findViewById(R.id.main);
        desert = findViewById(R.id.desert);

        DB = new DBHelper(this);



    }
}