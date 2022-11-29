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
import com.hsalf.smilerating.SmileRating;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmojiActivity extends AppCompatActivity {

    public SmileRating emo;
    public int get_value;
    DatabaseReference databaseReference;
    FirebaseDatabase refDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji);

        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);
        refDatabase= MyDatabaseUtil.getDatabase();
        databaseReference= refDatabase.getReference("PRODUCT/USERS/" +uid+ "/EMOJI");
        emo=(SmileRating) findViewById(R.id.smile_rating);



        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {

                switch (smiley){
                    case SmileRating.BAD:
                        Toast.makeText(EmojiActivity.this, "BAD", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(EmojiActivity.this, "GOOD", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(EmojiActivity.this, "GREAT", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(EmojiActivity.this, "OKAY", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(EmojiActivity.this, "TERRIBLE", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {

                Toast.makeText(EmojiActivity.this, "Selected rating" + level, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void done2(View view)
    {

        int value=(int)emo.getSelectedSmile();
        value=value+1;
        // Log.d("value1",""+value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String key =databaseReference.push().getKey();
        Star emo = new Star(value,currentDateandTime);

        // databaseReference.child(key).setValue(users);
        databaseReference.setValue(emo);

        Toast.makeText(getApplicationContext(),"Thanks for your feedback!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(EmojiActivity.this,MainActivity.class);
        startActivity(intent);

    }
}