package com.entertainerJatt.app.android;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.asyncTask.OtpVerificationAsyncTask;
import com.entertainerJatt.app.android.asyncTask.SignupAsycTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.fragments.CountryPicker;
import com.entertainerJatt.app.android.interfaces.CountryPickerListener;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.models.Country;
import com.entertainerJatt.app.android.notification.FireIDService;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileVerfificationActivity extends AppCompatActivity implements View.OnClickListener, IAsyncTask {
    private TextView LableTextView;
    private TextView countryTextView;
    private CountryPicker mCountryPicker;
    private ImageView flagImageView;
    private Button connectButton;
    private EditText MobileEditText;
    private String TAG = "MobileActivity";
    private String countryCode;
    private CountDownTimer countDownTimer;
    private Context context;
    private LinearLayout offKeyBoard;
    static Activity activity;
    private Global global;
    static EditText otpEditText;
    String phonee;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileverification);
        assignIds();
        listners();
    }

    private void listners() {
        countryTextView.setOnClickListener(this);
        offKeyBoard.setOnClickListener(this);
        connectButton.setOnClickListener(this);
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                countryTextView.setText(name + "(" + dialCode + ")");
                flagImageView.setImageResource(flagDrawableResID);
                countryCode = dialCode;

                mCountryPicker.dismiss();
            }
        });

    }

    private void assignIds() {
        context = this;
        global = (Global) getApplicationContext();

//        String tkn = FirebaseInstanceId.getInstance().getToken();

        activity = MobileVerfificationActivity.this;
        offKeyBoard = (LinearLayout) findViewById(R.id.offKeyBoard);
        LableTextView = (TextView) findViewById(R.id.LableTextView);
        flagImageView = (ImageView) findViewById(R.id.flagImageView);
        countryTextView = (TextView) findViewById(R.id.countryTextView);
        connectButton = (Button) findViewById(R.id.connectButton);
        MobileEditText = (EditText) findViewById(R.id.MobileEditText);
        String first = "Enter Your Mobile Number and\nConnect With Us to Play ";
        String next = "<font color='#CE3D47'>Free Music</font>";
        LableTextView.setText(Html.fromHtml(first + next));
        mCountryPicker = CountryPicker.newInstance("Select Country");
        startService(new Intent(context, FireIDService.class));
        getUserCountryInfo();
//        AboutUsAsynTask aboutUsAsynTask = new AboutUsAsynTask(MobileVerfificationActivity.this);
//        aboutUsAsynTask.execute();
        isSMSPermissionGranted();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectButton:

                mobileNumber = MobileEditText.getText().toString();
                if (TextUtils.isEmpty(countryCode)) {
                    Toast.makeText(context, "Please select Country ", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_LONG).show();
                } else if (mobileNumber.length() == 10) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setMobileNumber(countryCode + "-" + mobileNumber);
                    phonee = countryCode + "-" + mobileNumber;
//                    String uniqueId = global.getUniqueId();
                    String uniqueId = Util.getUserRToken(context);
                    //Log.i("uniqueId  ", uniqueId);
                    userInfo.setCallbackToken(uniqueId);
                    SignupAsycTask signupAsycTask = new SignupAsycTask(userInfo, MobileVerfificationActivity.this);
                    signupAsycTask.execute();
                } else {
                    Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.countryTextView:
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                break;
            case R.id.offKeyBoard:
                Activity activity = this;
                hideSoftKeyboard(activity);
                break;
        }
    }


    public void setText(String text) {
        otpEditText.setText(text);
        String code = otpEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            UserInfo userInfo = new UserInfo();
            userInfo.setRid(Util.getRId(context));
            userInfo.setConfirmation_code(code);
            OtpVerificationAsyncTask otpVerificationAsyncTask = new OtpVerificationAsyncTask(userInfo, MobileVerfificationActivity.this);
            otpVerificationAsyncTask.execute();
        } else {
            Toast.makeText(context, "Please enter OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserCountryInfo() {
        Country country = mCountryPicker.getUserCountryInfo(this);
        countryTextView.setText(country.getName() + "(" + country.getDialCode() + ")");
        flagImageView.setImageResource(country.getFlag());
        countryCode = country.getDialCode();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public void OnPreExecute() {
        Util.showDialog(context);
    }

    @Override
    public void OnPostExecute(String URL, JSONObject jsonObject) {
        Util.dismissDialog();
        Log.i("Response: ", TAG + " ::" + jsonObject.toString());
        if (URL.equalsIgnoreCase(ApplicationConstants.OTP_VERIFICATION)) {
            try {
                String userName = jsonObject.getString("username");
                String token = jsonObject.getString("token");
                Util.setUserToken(context, token);
                Util.setUserName(context, userName);

                startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP));
                onBackPressed();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (URL.equalsIgnoreCase(ApplicationConstants.REGISTER)) {
            try {
                String id = jsonObject.getString("rid");
                Util.setRId(context, id);
                ShowDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void ShowDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.otp_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        otpEditText = (EditText) dialog.findViewById(R.id.otpEditText);
        TextView LableTextView = (TextView) dialog.findViewById(R.id.LableTextView);
        final TextView textTimer = (TextView) dialog.findViewById(R.id.textTimer);
        TextView textResend = (TextView) dialog.findViewById(R.id.textResend);
        textResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = new UserInfo();
                userInfo.setMobileNumber(countryCode + "-" + mobileNumber);
                phonee = countryCode + "-" + mobileNumber;
                String uniqueId = Util.getUserRToken(context);
                userInfo.setCallbackToken(uniqueId);
                SignupAsycTask signupAsycTask = new SignupAsycTask(userInfo, MobileVerfificationActivity.this);
                signupAsycTask.execute();
            }
        });
        LableTextView.setText("We have sent an OTP to your mobile number\n" + phonee);
        ImageView imageClose = (ImageView) dialog.findViewById(R.id.imageClose);
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button connectButton = (Button) dialog.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String code = otpEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setRid(Util.getRId(context));
                    userInfo.setConfirmation_code(code);
                    OtpVerificationAsyncTask otpVerificationAsyncTask = new OtpVerificationAsyncTask(userInfo, MobileVerfificationActivity.this);
                    otpVerificationAsyncTask.execute();
                } else {
                    Toast.makeText(MobileVerfificationActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                long data = millisUntilFinished / 1000;
                String time = String.valueOf(data);
                if (time.length() == 3) {
                    String timefirst = time.substring(0, 1);
                    String timeLast = time.substring(time.length() - 2);
                    int timme = Integer.parseInt(timeLast) + 40;
                    time = "0" + timefirst + ":" + timme;
                }
                if (time.length() == 2) {
                    if (data > 60) {
                        int value = (int) (data - 60);
                        if (String.valueOf(value).length() == 1) {
                            time = "01:0" + value;
                        } else {
                            time = "01:" + value;

                        }
                    } else {
                        time = "00:" + time;
                    }
                }
                if (time.length() == 1) {
                    time = "00:0" + time;
                }
                textTimer.setText(time);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                textTimer.setText("done!");
            }

        }.start();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                countDownTimer.cancel();
            }
        });
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        if (Message.contains("500")) {

            Toast.makeText(context, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
