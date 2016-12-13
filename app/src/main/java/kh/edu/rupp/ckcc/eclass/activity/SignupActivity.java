package kh.edu.rupp.ckcc.eclass.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Locale;

import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 6/12/16.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LANGUAGE_CODE_ENGLISH = "en";
    private final String LANGUAGE_CODE_KHMER = "km";
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        findViewById(R.id.btn_signup).setOnClickListener(this);
    }

    private void switchLanguage() {
        if (currentLanguage == null || currentLanguage.equals(LANGUAGE_CODE_ENGLISH)) {
            currentLanguage = LANGUAGE_CODE_KHMER;
        } else {
            currentLanguage = LANGUAGE_CODE_ENGLISH;
        }
        Locale locale = new Locale(currentLanguage);
        Configuration configuration = getResources().getConfiguration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signup) {
            switchLanguage();
        }
    }
}
