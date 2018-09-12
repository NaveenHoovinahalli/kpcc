package teli.com.kpcc.Utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by madhuri on 4/1/15.
 */
public class ValidationUtils {
    public static boolean isValidEmail(EditText editText) {
        return !isTextEmpty(editText) && isValidEmail(editText.getText().toString());
    }

    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    public static boolean isTextEmpty(String string) {
        return TextUtils.isEmpty(string);
    }
}
