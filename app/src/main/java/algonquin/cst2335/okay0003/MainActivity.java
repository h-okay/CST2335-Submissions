package algonquin.cst2335.okay0003;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import android.os.Bundle;

import algonquin.cst2335.okay0003.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());


        TextView mytext = variableBinding.textview;
        Button mybutton = variableBinding.mybutton;
        EditText myedit = variableBinding.myedittext;

        if (mybutton != null)
            String editString = myedit
            mybutton.setOnClickListener(vw -> mytext.setText("Your edit text has: " + editString));

    }
}