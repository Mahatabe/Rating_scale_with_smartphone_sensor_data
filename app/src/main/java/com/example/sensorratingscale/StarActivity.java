package com.example.sensorratingscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StarActivity extends AppCompatActivity {

    public RatingBar rtn_bar;
    public int get_value;
    DatabaseReference databaseReference;
    FirebaseDatabase refDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);
        refDatabase= MyDatabaseUtil.getDatabase();
        databaseReference= refDatabase.getReference("PRODUCT/USERS/" +uid+ "/STAR");
        rtn_bar=(RatingBar) findViewById(R.id.ratingbarid);



    }
    public void done(View view)
    {
        int value=(int)rtn_bar.getRating();
       // Log.d("value1",""+value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String key =databaseReference.push().getKey();
        Star star = new Star(value,currentDateandTime);

        // databaseReference.child(key).setValue(users);
        databaseReference.setValue(star);

        Toast.makeText(getApplicationContext(),"Thanks for your feedback!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(StarActivity.this,MainActivity.class);
        startActivity(intent);

    }
}