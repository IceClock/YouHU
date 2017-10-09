package com.example.abood.youhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.abood.youhu.R;


public class Settings extends Fragment implements View.OnClickListener {
View view;
    public Settings() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_settings, container, false);
        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        return view;

    }




    @Override
    public void onClick(View v) {
        Intent startIntent = new Intent(getActivity(), LoginActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(startIntent);
        getActivity().finish();

    }
}