package com.example.fooday.ui.application.admin.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;
import com.example.fooday.db.query.Food;

import java.util.List;


public class AddFoodFragment extends Fragment {

    AutoCompleteTextView foodType,day;
    EditText foodName;
    Button addFood;
    View view;
    DBHelper DB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        String username = extras.getString("username");


        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_food, container, false);
        initComponent();
        List<String> foodTypeList = DB.GetAllFoodTypes();
        List<String> dayList = DB.GetDays();

        foodType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foodTypeList));
        day.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dayList));

        foodType.setOnClickListener(v -> foodType.showDropDown());

        day.setOnClickListener(v -> day.showDropDown());


        addFood.setOnClickListener(v -> {
            try {
                if(foodName.getText().toString().isEmpty() || foodType.getText().toString().isEmpty() || day.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    DB.insertFood(foodName.getText().toString(), foodType.getText().toString(), day.getText().toString());
                    Toast.makeText(getContext(), "Food added successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error adding food", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void initComponent() {
        foodType=view.findViewById(R.id.addFoodType);
        foodName=view.findViewById(R.id.foodName);
        addFood=view.findViewById(R.id.updateFood);
        day=view.findViewById(R.id.fooday);
        DB=new DBHelper(getContext());
    }
}