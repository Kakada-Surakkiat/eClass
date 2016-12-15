package kh.edu.rupp.ckcc.eclass.utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class MyNetwork {

    private static MyNetwork INSTANCE;
    private RequestQueue requestQueue;
    private Gson gson;

    private MyNetwork(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        gson = new Gson();
    }

    public static MyNetwork getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyNetwork(context);
        }
        return INSTANCE;
    }

    public void addRequest(Request request) {
        requestQueue.add(request);
    }

    public Gson getGson(){
        return gson;
    }

}
