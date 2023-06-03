package algonquin.cst2335.okay0003;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    TextView welcomeMessage;
    EditText phoneNumber;
    Button callNumberButton;
    Button changePictureButton;
    ImageView profileImage;
    private static final String FILENAME = "Picture.png";

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber.getText().toString());
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        welcomeMessage = findViewById(R.id.welcomeView);
        phoneNumber = findViewById(R.id.editTextPhone);
        callNumberButton = findViewById(R.id.callNumberButton);
        changePictureButton = findViewById(R.id.changePictureButton);
        profileImage = findViewById(R.id.profileImage);

        String savedPhoneNumber = prefs.getString("phoneNumber", "");
        phoneNumber.setText(savedPhoneNumber);

        File file = new File(getFilesDir(), FILENAME);

        if (file.exists()) {
            Log.d("SecondActivity", "HERE");
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        welcomeMessage.setText("Welcome back " + emailAddress);

        callNumberButton.setOnClickListener(click -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        //had to take it out of event listener so it is saved. Otherwise gives STATE error.
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        profileImage.setImageBitmap(thumbnail);

                        FileOutputStream fOut = null;
                        try {
                            fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        changePictureButton.setOnClickListener(click -> {
            cameraResult.launch(cameraIntent);

        });
    }
}