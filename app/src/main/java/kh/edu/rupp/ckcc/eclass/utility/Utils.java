package kh.edu.rupp.ckcc.eclass.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kh.edu.rupp.ckcc.eclass.vo.User;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class Utils {

    private final static String KEY_USER = "user";

    public static void storeUser(Context context, User user){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String serializedUser = MyNetwork.getInstance(context).getGson().toJson(user);
        preference.edit().putString(KEY_USER, serializedUser).commit();
    }

    public static User getUser(Context context){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String serializedUser = preference.getString(KEY_USER, null);
        if(serializedUser == null){
            return null;
        }else{
            User user = MyNetwork.getInstance(context).getGson().fromJson(serializedUser, User.class);
            return user;
        }
    }

}
