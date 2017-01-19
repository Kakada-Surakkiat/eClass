package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.utility.MyNetwork;
import kh.edu.rupp.ckcc.eclass.utility.Utils;
import kh.edu.rupp.ckcc.eclass.vo.User;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final int CHOOSE_IMAGE_REQUEST_CODE = 1;

    private CircleImageView imgProfile;
    private TextView txtName;
    private TextView txtGender;
    private TextView txtEmail;
    private TextView txtDob;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txt_name_value);
        txtGender = (TextView) findViewById(R.id.txt_gender_value);
        txtEmail = (TextView) findViewById(R.id.txt_email_value);
        txtDob = (TextView) findViewById(R.id.txt_dob_value);
        user = Utils.getUser(this);
        setProfileImage();
        loadProfileInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            setImageFromGallery(data.getData());
            uploadImage(data.getData());
        }
    }

    private void setProfileImage(){
        if(user.isUploadedProfile()){
            setProfileImageFromFirebase();
        }else{
            setProfileImageFromFacebook();
        }
    }

    private void setProfileImageFromFirebase(){
        String fileName = user.getId() + ".jpg";
        String referenceString = "images/" + fileName;
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference(referenceString);
        profileImageRef.getBytes(1024*1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                Bitmap bitmap = convertBytesToBitmap(task.getResult());
                imgProfile.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap convertBytesToBitmap(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void setProfileImageFromFacebook(){
        Profile profile = Profile.getCurrentProfile();
        String profileImageUrl = profile.getProfilePictureUri(120, 120).toString();
        setProfileImageFromUrl(profileImageUrl);
    }

    private void setProfileImageFromUrl(String imageUrl){
        Log.d("ckcc", "setProfileImageFromUrl: " + imageUrl);
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.img_profile){
            chooseImageFromGallery();
        }
    }

    private void chooseImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST_CODE);
    }

    private void setImageFromGallery(Uri selectedImage){
        try {
            Bitmap bitmap = decodeUri(selectedImage, 200);
            imgProfile.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(Uri selectedImage){
        User user = Utils.getUser(this);
        String fileName = user.getId() + ".jpg";
        String referenceString = "images/" + fileName;
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference(referenceString);
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
            UploadTask task = profileImageRef.putStream(inputStream);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("ckcc", "Upload image success");
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ckcc", "Upload image fail: " + e.getMessage());
                }
            });
        } catch (FileNotFoundException e) {
            Log.d("ckcc", "Open input stream error: " + e.getMessage());
        }
    }

    private Bitmap decodeUri(Uri selectedImage, int size) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

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
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

}
