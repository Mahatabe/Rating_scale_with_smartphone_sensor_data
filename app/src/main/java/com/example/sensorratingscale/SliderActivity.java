package com.example.sensorratingscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.SmileRating;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SliderActivity extends AppCompatActivity {

    public Slider sl;
    public int get_value;
    DatabaseReference databaseReference;
    FirebaseDatabase refDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);
        refDatabase= MyDatabaseUtil.getDatabase();
        databaseReference= refDatabase.getReference("PRODUCT/USERS/" +uid+ "/SLIDER");
        sl=(Slider) findViewById(R.id.slide);

    }
    public void done4(View view)
    {

        int value=(int)sl.getValue();
        // Log.d("value1",""+value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String key =databaseReference.push().getKey();
        Star s = new Star(value,currentDateandTime);

        // databaseReference.child(key).setValue(users);
        databaseReference.setValue(s);

        Toast.makeText(getApplicationContext(),"Thanks for your feedback!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SliderActivity.this,MainActivity.class);
        startActivity(intent);

    }
}