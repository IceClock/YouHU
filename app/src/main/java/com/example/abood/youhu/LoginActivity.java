package com.example.abood.youhu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

private static Button button;
private static EditText id , pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.userid);
        pass = (EditText) findViewById(R.id.password);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (id.getText().toString().equals("123") && pass.getText().toString().equals("123")){
                            Intent intent = new Intent("com.example.abood.youhu.HomeActivity");
                            startActivity(intent);
                            }

                        else if(id.getText().toString().equals("1234") && pass.getText().toString().equals("1234")) {
                            Intent intent = new Intent("com.example.abood.youhu.HomeAdmin");
                            startActivity(intent);
                            }

                        else{Toast.makeText(LoginActivity.this,"Wrong ID or Password",Toast.LENGTH_SHORT).show();}



                        }


                }

        );
        //OnClickButtonListener();
    }

    public void OnClickButtonListener()
    {

    }



}
