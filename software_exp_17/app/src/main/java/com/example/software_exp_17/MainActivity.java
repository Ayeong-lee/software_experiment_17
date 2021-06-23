package com.example.software_exp_17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.software_exp_17.DBHelper.DATABASE_VERSION;

public class MainActivity extends AppCompatActivity {
    Button logoutbtn;


    static DBHelper helper;
    static SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //db 연결

        helper = new DBHelper(getApplicationContext(), DATABASE_VERSION);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startLoginActivity();
        }else{
            Intent intent2 = new Intent(this, MainPlan.class);
            startActivity(intent2);
        }

        logoutbtn = (Button) findViewById(R.id.logoutButton);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
            }
        });

    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}