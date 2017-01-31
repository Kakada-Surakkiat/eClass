package kh.edu.rupp.ckcc.eclass.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * eClass
 * Created by leapkh on 9/1/17.
 */

public class EclassMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d("ckcc", "onMessageReceived: " + title + " - " + message);
    }

}
