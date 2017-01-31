package kh.edu.rupp.ckcc.eclass.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.io.FileNotFoundException;

import kh.edu.rupp.ckcc.eclass.vo.User;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class Utils {

    private final static String KEY_USER = "user";

    public static void storeUser(Context context, User user) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String serializedUser = MyNetwork.getInstance(context).getGson().toJson(user);
        preference.edit().putString(KEY_USER, serializedUser).commit();
    }

    public static User getUser(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String serializedUser = preference.getString(KEY_USER, null);
        if (serializedUser == null) {
            return new User();
        } else {
            User user = MyNetwork.getInstance(context).getGson().fromJson(serializedUser, User.class);
            return user;
        }
    }

    public static Bitmap decodeUri(Context context, Uri selectedImage, int size) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < size || height_tmp / 2 < size) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
