package com.example.fooday.ui.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.fooday.ui.application.admin.AdminActivity;
import com.example.fooday.ui.application.user.UserActivity;
import com.example.fooday.R;
import com.example.fooday.db.DBHelper;

import java.time.DayOfWeek;
import java.util.Date;


public class SignUpFragment extends Fragment {


    Button register_btn;
    EditText username, password, email;
    Switch switcher;
    DBHelper DB;
    View view;
    int role = 0;
    Date date = new Date();
    DayOfWeek dayOfWeek;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.signup_layout, container, false);

        register_btn =(Button) view.findViewById(R.id.loginbtn);
        username =(EditText) view.findViewById(R.id.username);
        password =(EditText) view.findViewById(R.id.password);
        email =(EditText) view.findViewById(R.id.email_userName);
        switcher = (Switch) view.findViewById(R.id.swRole);
        DB = new DBHelper(getContext());

       switcher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(switcher.isChecked()){
                        role = 2;
                    }else{
                        role = 1;
                    }
                }
            });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String mail = email.getText().toString();
                String roleVal = String.valueOf(role);

                if(user.equals("")||pass.equals("")||mail.equals("")||roleVal.equals("0"))
                    RegisterActivity.alertBox("Please enter all the fields", getContext());

                else{
                    Boolean checkuser = DB.checkUsernameEmail(user, mail);
                    if(checkuser==false){
                        Boolean insert = DB.insertRegisterData(user,pass,mail,roleVal);
                        if(insert==true){
                            RegisterActivity.alertBox("Registered successfully"+date.getDay(), getContext());
                            goHomeScreen(user);

                        }else{
                           RegisterActivity.alertBox("Registration failed", getContext());
                        }
                    }else{
                        RegisterActivity.alertBox("User already exists! please sign in", getContext());
                    }
                }
            }


        });

        return view;

    }

    private void goHomeScreen(String user) {
        if (!DB.IsAdmin(user)){
            RegisterActivity.alertBox("You Signed Up as User", getContext());
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("username", user);
            startActivity(intent);
        }else {
            RegisterActivity.alertBox("You Signed Up as Admin", getContext());
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            intent.putExtra("username", user);
            startActivity(intent);
        }
    }


}