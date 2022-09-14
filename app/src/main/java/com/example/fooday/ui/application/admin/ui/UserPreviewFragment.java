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


public class UserPreviewFragment extends Fragment {

    Button orderNow;
    TextView todayMeal;
    AutoCompleteTextView appetizer,mainCourse,desert;
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
            List<String> appe = DB.GetFoodDayByType("Appetizer",date.getDay());
            List<String> main = DB.GetFoodDayByType("Main Course",date.getDay());
            List<String> des = DB.GetFoodDayByType("Desert",date.getDay());

            appetizer.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, appe));
            mainCourse.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, main));
            desert.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, des));

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


            viewData(day.toString());

        } catch (Exception e) {

            Toast.makeText(getContext(), "No Meals For Today Yet", Toast.LENGTH_SHORT).show();
        }


        orderNow.setOnClickListener(v -> {
            try {
                if (appetizer.getText().toString().isEmpty() || mainCourse.getText().toString().isEmpty() || desert.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Select Your Order", Toast.LENGTH_SHORT).show();
                } else {
                DB.InsertOrder(appetizer.getText().toString(),
                        date.getDay(),"Appetizer",username);
                DB.InsertOrder(mainCourse.getText().toString(),
                        date.getDay(),"Main Course",username);
                DB.InsertOrder(desert.getText().toString(),
                        date.getDay(),"Desert",username);

                Toast.makeText(getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(getContext(), "Error placing order", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

    @SuppressLint("SetTextI18n")
    private void viewData(String day) {
        todayMeal.setText(day + "'s meal");
        //breakfastMeal.setText(DB.getBreakFastByDay(date.getDay()));
        //lunchMeal.setText(DB.getLunchByDay(date.getDay()));
        //dinnerMeal.setText(DB.getDinnerByDay(date.getDay()));
    }



    private void initComponent() {

        orderNow = view.findViewById(R.id.order);
        todayMeal = view.findViewById(R.id.meals);
        appetizer = view.findViewById(R.id.appetizer);
        mainCourse = view.findViewById(R.id.main);
        desert = view.findViewById(R.id.desert);

        DB = new DBHelper(getContext());
    }

}