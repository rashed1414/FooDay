package com.example.fooday.ui.application.admin.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;

import java.util.List;


public class UpdateFoodFragment extends Fragment {

    AutoCompleteTextView day,breakfast,lunch,dinner;
    Button update;
    View view;
    DBHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        String username = extras.getString("username");

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_update_food, container, false);
        initComponent();
        List<String> dayList = DB.GetDays();
        List<String> breakfastList = DB.GetFoodByType("Breakfast");
        List<String> lunchList = DB.GetFoodByType("Lunch");
        List<String> dinnerList = DB.GetFoodByType("Dinner");

        day.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dayList));
        breakfast.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, breakfastList));
        lunch.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lunchList));
        dinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dinnerList));



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DB.UpdateDayFood(breakfast.getText().toString(),day.getText().toString(),"Breakfast");
                    DB.UpdateDayFood(lunch.getText().toString(),day.getText().toString(),"Lunch");
                    DB.UpdateDayFood(dinner.getText().toString(),day.getText().toString(),"Dinner");
                    Toast.makeText(getContext(), "Food updated successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println(e.toString());
                    Toast.makeText(getContext(), "Error updating food", Toast.LENGTH_SHORT).show();
                }
            }
        });
    return view;

    }

    private void initComponent() {
        day=view.findViewById(R.id.day);
        breakfast=view.findViewById(R.id.autoBreakfast);
        lunch=view.findViewById(R.id.autoLunch);
        dinner=view.findViewById(R.id.autoDinner);
        update=view.findViewById(R.id.updateFood);
        DB=new DBHelper(getContext());

    }
}