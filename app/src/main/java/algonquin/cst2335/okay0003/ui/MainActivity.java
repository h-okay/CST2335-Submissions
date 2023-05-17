package algonquin.cst2335.okay0003.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageButton;

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

        model.btn.observe(this, (selected) -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);

            Boolean isChecked = variableBinding.checkBox.isChecked();
            CharSequence text = "The value is now: " + isChecked;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        });

        variableBinding.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            model.btn.postValue(variableBinding.checkBox.isChecked());
        });

        variableBinding.radioButton.setOnCheckedChangeListener((btn, isChecked) -> {
            model.btn.postValue(variableBinding.radioButton.isChecked());
        });

        variableBinding.switch1.setOnCheckedChangeListener((btn, isChecked) -> {
            model.btn.postValue(variableBinding.switch1.isChecked());
        });

        variableBinding.myimagebutton.setOnClickListener(click -> {
            int height = variableBinding.acImage.getHeight();
            int width = variableBinding.acImage.getWidth();
            model.imgHeight.postValue(height);
            model.imgWidth.postValue(width);

            CharSequence text = "The width = " + width + " and height = " + height;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        });
    }
}