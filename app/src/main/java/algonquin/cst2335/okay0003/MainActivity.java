package algonquin.cst2335.okay0003;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.okay0003.databinding.ActivityMainBinding;

/**
 * This activity retrieves weather information from the OpenWeatherMap API and
 * displays the current temperature, weather description, and an icon representing
 * the weather condition for a specified city. This implementation includes checking if the
 * weather icon already exists on the device before making a request to download it.
 * If the icon exists, it is loaded directly from the device storage, saving network requests.
 *
 * @author Hakan Okay
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private static final String stringURL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s";
    private static final String imageURL = "http://openweathermap.org/img/w/%s.png";
    private static final String apiKey = "798aeefb89d673f586865c6a45fed220";
    protected String cityName;
    private RequestQueue queue;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.getForecast.setOnClickListener(click -> {
            cityName = binding.input.getText().toString();
            JsonObjectRequest request;
            try {
                request = new JsonObjectRequest(
                        Request.Method.GET,
                        String.format(
                                stringURL,
                                URLEncoder.encode(cityName, "UTF-8"),
                                apiKey,
                                "metric"),
                        null,
                        response -> {
                            try {
                                updateViews(response, binding);
                                JSONArray weatherArray = response.getJSONArray("weather");
                                JSONObject position0 = weatherArray.getJSONObject(0);
                                String iconName = position0.getString("icon");
                                boolean imageExists = checkIfImageExists(iconName);

                                if (imageExists) {
                                    loadImageFromStorage(iconName, binding.icon);
                                } else {
                                    ImageRequest imgReq = new ImageRequest(
                                            String.format(imageURL, iconName),
                                            responseBitmap -> {
                                                saveImageToStorage(iconName, responseBitmap);
                                                loadImageFromStorage(iconName, binding.icon);
                                            },
                                            1024,
                                            1024,
                                            null,
                                            Bitmap.Config.RGB_565,
                                            error -> showToastMessage("Failed to load weather icon.")
                                    );
                                    queue.add(imgReq);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        error -> showToastMessage("Failed to retrieve weather data.")
                );
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            queue.add(request);
        });
    }

    /**
     * Checks if the image with the given name exists on the device.
     *
     * @param imageName the name of the image to check
     * @return true if the image exists, false otherwise
     */
    private boolean checkIfImageExists(String imageName) {
        File file = new File(getFilesDir(), imageName + ".png");
        return file.exists();
    }

    /**
     * Saves the given image to the device with the specified name.
     *
     * @param imageName   the name of the image to save
     * @param imageBitmap the image to save
     */
    private void saveImageToStorage(String imageName, Bitmap imageBitmap) {
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(imageName + ".png", Context.MODE_PRIVATE);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Loads the image with the specified name from the device storage into the ImageView.
     *
     * @param imageName the name of the image to load
     * @param imageView the ImageView to display the loaded image
     */
    private void loadImageFromStorage(String imageName, ImageView imageView) {
        try {
            File f = new File(getFilesDir(), imageName + ".png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a toast message with the given text.
     *
     * @param message the text to display in the toast message
     */
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Update views on the screen based on the response.
     *
     * @param response response received from the request
     * @param binding  binding to be updated
     */
    @SuppressLint("SetTextI18n")
    private void updateViews(JSONObject response, ActivityMainBinding binding) {
        try {
            // Retrieve weather data
            JSONObject mainObject = response.getJSONObject("main");
            double current = mainObject.getDouble("temp");
            double min = mainObject.getDouble("temp_min");
            double max = mainObject.getDouble("temp_max");
            int humidity = mainObject.getInt("humidity");

            JSONArray weatherArray = response.getJSONArray("weather");
            JSONObject position0 = weatherArray.getJSONObject(0);
            String description = position0.getString("description");

            // Set the values to the TextViews
            runOnUiThread(() -> {
                binding.temp.setText("Current temperature is: " + current);
                binding.min.setText("The minimum temperature is: " + min);
                binding.max.setText("The maximum temperature is: " + max);
                binding.hum.setText("The humidity is: " + humidity);
                binding.desc.setText(description);

                // Make the TextViews visible
                binding.temp.setVisibility(View.VISIBLE);
                binding.min.setVisibility(View.VISIBLE);
                binding.max.setVisibility(View.VISIBLE);
                binding.hum.setVisibility(View.VISIBLE);
                binding.desc.setVisibility(View.VISIBLE);
                binding.icon.setVisibility(View.VISIBLE);
            });
        } catch (JSONException e) {
            e.printStackTrace();
            showToastMessage("Failed to parse weather data.");
        }
    }
}
