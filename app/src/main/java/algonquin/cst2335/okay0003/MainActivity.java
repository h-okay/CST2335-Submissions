package algonquin.cst2335.okay0003;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);

        mybutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String editString = myedit.getText().toString();
                mytext.setText( "Your edit text has: " + editString);
            }
        });
    }
}