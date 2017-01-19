package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 29/12/16.
 */

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseRemoteConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_contact);
        findViewById(R.id.btn_view_on_map).setOnClickListener(this);
        findViewById(R.id.btn_view_website).setOnClickListener(this);

        config = FirebaseRemoteConfig.getInstance();
        config.setDefaults(R.xml.default_configuration);
        config.fetch(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("ckcc", "Fetch config success.");
                    config.activateFetched();
                }else{
                    Log.d("ckcc", "Fetch config fail: " + task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_view_on_map) {
            startActivity(new Intent(this, CkccMapActivity.class));
        }else if(v.getId() == R.id.btn_view_website){
            String webUrl = config.getString("ckcc_web_url");
            Log.d("ckcc", "Web url: " + webUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(intent);
        }
    }
}
