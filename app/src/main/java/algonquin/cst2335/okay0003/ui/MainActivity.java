package algonquin.cst2335.okay0003.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import algonquin.cst2335.okay0003.R;

/**
 * The main activity of the application that allows users to input a password and
 * checks its complexity based on certain criteria. It displays the result of the
 * password complexity check and provides toast notifications for each complexity
 * requirement that is not met.
 *
 * @author Hakan Okay
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The TextView used to display the result message.
     */
    private TextView tv = null;

    /**
     * The EditText used to input the password.
     */
    private EditText et = null;

    /**
     * The Button used to initiate the password check.
     */
    private Button btn = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.input);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(click -> {
            String password = et.getText().toString();
            boolean isValidPassword = checkPasswordComplexity(password);
            if (isValidPassword) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });
    }

    /**
     * Checks the complexity of a password by verifying if it contains a lowercase letter,
     * an uppercase letter, a number, and a special character. Displays toast notifications
     * for each complexity requirement that is not met.
     *
     * @param password the password to be checked
     * @return true if the password is complex enough, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        String lowercasePattern = "^(?=.*[a-z]).+$";
        String uppercasePattern = "^(?=.*[A-Z]).+$";
        String numberPattern = "^(?=.*\\d).+$";
        String specialPattern = "^(?=.*[#$%^&*!@?]).+$";

        hasLowercase = Pattern.compile(lowercasePattern).matcher(password).matches();
        hasUppercase = Pattern.compile(uppercasePattern).matcher(password).matches();
        hasNumber = Pattern.compile(numberPattern).matcher(password).matches();
        hasSpecial = Pattern.compile(specialPattern).matcher(password).matches();

        if (!hasLowercase) {
            showToastMessage("Your password does not have a lowercase letter.");
        }

        if (!hasUppercase) {
            showToastMessage("Your password does not have an uppercase letter.");
        }

        if (!hasNumber) {
            showToastMessage("Your password does not have a number.");
        }

        if (!hasSpecial) {
            showToastMessage("Your password does not have a special character.");
        }

        return hasLowercase && hasUppercase && hasNumber && hasSpecial;
    }

    /**
     * Displays a toast message with the given text.
     *
     * @param message the text to display in the toast message
     */
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}