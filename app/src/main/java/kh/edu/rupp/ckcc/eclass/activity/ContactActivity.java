package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 29/12/16.
 */

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_contact);
        findViewById(R.id.btn_view_on_map).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, CkccMapActivity.class));
    }
}
