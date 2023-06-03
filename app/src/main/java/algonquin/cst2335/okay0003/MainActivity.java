package algonquin.cst2335.okay0003;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

import android.os.Bundle;
import android.widget.EditText;

import org.w3c.dom.Text;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button loginButton;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        Log.w(TAG, "In onCreate() - Loading Widgets");

        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailEditText);
        emailEditText.setText(emailAddress);

        loginButton.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", emailEditText.getText().toString());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", emailEditText.getText().toString());
            editor.apply();

            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart() - The application is now visible on screen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause() - The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "In onDestroy() - Any memory used by the application is freed");
    }


}