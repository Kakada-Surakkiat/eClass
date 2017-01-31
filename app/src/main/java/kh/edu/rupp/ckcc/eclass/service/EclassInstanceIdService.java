package kh.edu.rupp.ckcc.eclass.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * eClass
 * Created by leapkh on 9/1/17.
 */

public class EclassInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.d("ckcc", "onTokenRefresh: " + FirebaseInstanceId.getInstance().getToken());
    }
}
