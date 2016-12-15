package kh.edu.rupp.ckcc.eclass.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.utility.MyNetwork;
import kh.edu.rupp.ckcc.eclass.utility.Utils;
import kh.edu.rupp.ckcc.eclass.vo.User;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imgProfile;
    private TextView txtName;
    private TextView txtGender;
    private TextView txtEmail;
    private TextView txtDob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        txtName = (TextView) findViewById(R.id.txt_name_value);
        txtGender = (TextView) findViewById(R.id.txt_gender_value);
        txtEmail = (TextView) findViewById(R.id.txt_email_value);
        txtDob = (TextView) findViewById(R.id.txt_dob_value);
        setProfileImage();
        loadProfileInfo();
    }

    private void setProfileImage(){
        Profile profile = Profile.getCurrentProfile();
        String profileImageUrl = profile.getProfilePictureUri(120, 120).toString();
        ImageRequest imageRequest = new ImageRequest(profileImageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgProfile.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyNetwork.getInstance(this).addRequest(imageRequest);
    }

    private void loadProfileInfo(){
        if(Utils.getUser(this) == null){
            Log.d("ckcc", "load profile from facebook");
            AccessToken token = AccessToken.getCurrentAccessToken();
            GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("ckcc", "Response: " + object);
                    User user = User.fromJson(object);
                    setProfileInfo(user);
                    Utils.storeUser(ProfileActivity.this, user);
                }
            });
            Bundle params = new Bundle();
            params.putString("fields", "name, email, birthday, gender");
            graphRequest.setParameters(params);
            graphRequest.executeAsync();
        }else{
            Log.d("ckcc", "Use profile from preferene");
            User user = Utils.getUser(this);
            setProfileInfo(user);
        }
    }

    private void setProfileInfo(User user){
        txtName.setText(user.getName());
        txtGender.setText(user.getGender());
        txtEmail.setText(user.getEmail());
    }

}
