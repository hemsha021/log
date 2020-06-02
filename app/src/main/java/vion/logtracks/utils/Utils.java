package vion.logtracks.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {


    public static String ISSUCCESS = "success";
    public static String USERMODEL = "userModel";
    public static String ISLOGIN = "isLogin";
    public static String FIREBASETOKEN = "token";

    public static void changeStatusBarColor(Activity con, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = con.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);


        }
    }


    public static void changeStatusBarColor(Activity con) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = con.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }


    }

    public static void hideKeyboardFrom(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static float[] getRadius(float value) {
        return new float[]{value, value, value, value, value, value, value, value};
    }


    public static void showSnackBar(Activity con, String message) {
        hideKeyboardFrom(con);
        Snackbar.make(con.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public static void noInternetConnection(Activity activity) {
        Snackbar.make(activity.findViewById(android.R.id.content), "No internet Connection", Snackbar.LENGTH_LONG).show();
    }

    public static String setSubjectNames(JSONArray jsonArray) {
        String subs = "";
        for (int i = 0; i <jsonArray.length(); i++) {
            try {
                JSONObject jObj = new JSONObject(jsonArray.get(i).toString());
                subs += jObj.getString("subject") + ",";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return subs.substring(0, subs.length() - 1);

    }


}
