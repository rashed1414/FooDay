package com.example.fooday.ui.application.admin.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;

import java.time.DayOfWeek;
import java.util.Date;


public class UserPreviewFragment extends Fragment {

    Button orderNow;
    TextView todayMeal, breakfastMeal, lunchMeal, dinnerMeal, breakfast, lunch, dinner;
    DBHelper DB;
    View view;
    Date date;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        String username = extras.getString("username");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_preview, container, false);
        initComponent();

        date = new Date();
        DayOfWeek day = DayOfWeek.of(date.getDay());

        try {
            viewData(day.toString());

        } catch (Exception e) {

            Toast.makeText(getContext(), "Error loading meal", Toast.LENGTH_SHORT).show();
        }


        orderNow.setOnClickListener(v -> {
            try {
                DB.InsertOrder(breakfastMeal.getText().toString(),
                        date.getDay(),"Breakfast",username);
                DB.InsertOrder(lunchMeal.getText().toString(),
                        date.getDay(),"Lunch",username);
                DB.InsertOrder(dinnerMeal.getText().toString(),
                        date.getDay(),"Dinner",username);

                Toast.makeText(getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(), "Error placing order", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

    @SuppressLint("SetTextI18n")
    private void viewData(String day) {
        todayMeal.setText(day + "'s meal");
        breakfastMeal.setText(DB.getBreakFastByDay(date.getDay()));
        lunchMeal.setText(DB.getLunchByDay(date.getDay()));
        dinnerMeal.setText(DB.getDinnerByDay(date.getDay()));
    }



    private void initComponent() {

        orderNow = view.findViewById(R.id.order);
        todayMeal = view.findViewById(R.id.meals);
        breakfast = view.findViewById(R.id.breakfast);
        lunch = view.findViewById(R.id.lunch);
        dinner = view.findViewById(R.id.dinner);
        breakfastMeal = view.findViewById(R.id.breakfastMeal);
        lunchMeal = view.findViewById(R.id.lunchMeal);
        dinnerMeal = view.findViewById(R.id.dinnerMeal);
        DB = new DBHelper(getContext());
    }

}