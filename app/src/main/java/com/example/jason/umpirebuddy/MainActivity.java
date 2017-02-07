package com.example.jason.umpirebuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    static final int SETTINGS_REQUEST = 1;

    private static final String CALLS_KEY = "calls_key";
    private static final String PREFS = "preferences";
    private static final String OUT_KEY = "out_key";
    private static final String STRIKE_KEY = "strike_key";
    private static final String BALL_KEY = "ball_key";


    private Button strike;
    private Button ball;

    private String strikeString = "Strike: ";
    private String ballString = "Ball: ";
    private String totalStrikes = "Total Strikes: ";

    private TextView ballTextView;
    private TextView strikeTextView;
    private TextView outTextView;

    private static int strikeCount = 0;
    private static int ballCount = 0;
    private static int totalStrikeCount = 0;

    public void displayCounts(){
        strikeString = "Strike: " + strikeCount;
        strikeTextView.setText(strikeString);
        ballString = "Ball: " + ballCount;
        ballTextView.setText(ballString);
        totalStrikes = "Total Strikes: " + totalStrikeCount;
        outTextView.setText(totalStrikes);
    }

    public void incrementCount(Character c){
        if(c == 's'){
            strikeCount++;
        } else {
            ballCount++;
        }
        if(strikeCount == 3){
            Toast.makeText(MainActivity.this, R.string.out_toast, Toast.LENGTH_SHORT).show();
            totalStrikeCount++;
            resetCount();
        }
        if(ballCount == 4) {
            Toast.makeText(MainActivity.this, R.string.walk_toast, Toast.LENGTH_SHORT).show();
            resetCount();
        }
        displayCounts();
    }
    public void resetCount(){
        strikeCount = 0;
        ballCount = 0;
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Saves the out count into a preferences file when the onPause function is called
        SharedPreferences prefs = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(OUT_KEY, totalStrikeCount);
        editor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        strike = (Button) findViewById(R.id.strike);
        strike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                incrementCount('s');
            }
        });
        ball = (Button) findViewById(R.id.ball);
        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                incrementCount('b');
            }
        });

        ballTextView = (TextView) findViewById(R.id.ballCounter);
        strikeTextView = (TextView) findViewById(R.id.strikeCounter);
        outTextView = (TextView) findViewById(R.id.totalStrikes);
        displayCounts();

        if(savedInstanceState != null){
            strikeCount = savedInstanceState.getInt(STRIKE_KEY);
            ballCount = savedInstanceState.getInt(BALL_KEY);
            totalStrikeCount = savedInstanceState.getInt(OUT_KEY);
            displayCounts();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, 0);
        totalStrikeCount = prefs.getInt(OUT_KEY, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.

        switch (item.getItemId()) {
            case R.id.reset:
                resetCount();
                displayCounts();
                return true;

            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

