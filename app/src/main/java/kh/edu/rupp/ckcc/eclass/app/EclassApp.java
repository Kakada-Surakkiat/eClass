package kh.edu.rupp.ckcc.eclass.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * eClass
 * Created by leapkh on 5/1/17.
 */

public class EclassApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("ckcc", "App onCreate");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
