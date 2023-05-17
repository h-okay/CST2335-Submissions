package algonquin.cst2335.okay0003.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.okay0003.data.MainViewModel;
import algonquin.cst2335.okay0003.databinding.ActivityMainBinding;
import algonquin.cst2335.okay0003.data.MainViewModel;


import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.mybutton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });

        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit test has: " + s);
        });
    }
}