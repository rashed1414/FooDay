package com.example.fooday.ui.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fooday.R;
import com.example.fooday.db.DBHelper;
import com.example.fooday.ui.application.admin.AdminActivity;
import com.example.fooday.ui.application.user.UserActivity;


public class LogInFragment extends Fragment {

    Button login_btn;
    EditText userName_email, password;
    DBHelper DB;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.login_layout, container, false);

        login_btn =(Button) view.findViewById(R.id.loginbtn);
        userName_email =(EditText) view.findViewById(R.id.email_userName);
        password =(EditText) view.findViewById(R.id.password);
        DB = new DBHelper(getContext());
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userName_email.getText().toString();
                String pass = password.getText().toString();

                if(email.equals("")||pass.equals(""))
                    RegisterActivity.alertBox("Please enter all the fields", getContext());
                else{
                    boolean checkuserpass = DB.checkUsernamePassword(email, pass);
                    if(checkuserpass){
                       RegisterActivity.alertBox("Sign in successfull", getContext());
                       goHomeScreen(email);
                    }else{
                        RegisterActivity.alertBox("Invalid credentials", getContext());
                    }
                }
            }
        });


        return view;
    }

    private void goHomeScreen(String email) {
        if(!DB.IsAdmin(email)){
            RegisterActivity.alertBox("Welcome User", getContext());
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("username", email);
            startActivity(intent);
        }else {
            RegisterActivity.alertBox("Welcome Admin", getContext());
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            intent.putExtra("username", email);
            startActivity(intent);
        }
    }
}