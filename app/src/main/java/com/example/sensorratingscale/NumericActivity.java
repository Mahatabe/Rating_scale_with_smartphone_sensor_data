package com.example.sensorratingscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NumericActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    DatabaseReference databaseReference;
    FirebaseDatabase refDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);
        refDatabase= MyDatabaseUtil.getDatabase();
        databaseReference= refDatabase.getReference("PRODUCT/USERS/" +uid+ "/NUMERIC");
        radioGroup = findViewById(R.id.radio_id);
    }

    public void done5(View view)
    {

        int value=(int)radioGroup.getCheckedRadioButtonId();

       value=value%5;

       if(value==0){
           value=5;
       }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String key =databaseReference.push().getKey();
        Star nu = new Star(value,currentDateandTime);

        databaseReference.setValue(nu);

        Toast.makeText(getApplicationContext(),"Thanks for your feedback!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(NumericActivity.this,MainActivity.class);
        startActivity(intent);
    }
}