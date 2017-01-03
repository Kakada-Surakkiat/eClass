package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.fragment.CoursesFragment;
import kh.edu.rupp.ckcc.eclass.utility.MyNetwork;
import kh.edu.rupp.ckcc.eclass.view.NavHeaderView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavHeaderView.OnNavHeaderItemClick, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavHeaderView navHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        navHeaderView = new NavHeaderView(this);
        navHeaderView.setOnNavHeaderItemClick(this);
        navigationView.addHeaderView(navHeaderView);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setUserInfo();

        showCourses();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogoutClick() {
        Log.d("ckcc", "onLogoutClick");
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showCourses(){
        Log.d("ckcc", "Show courses");
        CoursesFragment fragment = new CoursesFragment();
        getFragmentManager().beginTransaction().replace(R.id.frm_content, fragment).commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_courses:
                showCourses();
                break;
            case R.id.mnu_share:
                shareToFacebook();
                break;
            case R.id.mnu_contact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
        }
        return false;
    }

    private void setUserInfo() {
        Profile profile = Profile.getCurrentProfile();
        navHeaderView.setUsername(profile.getName());
        String profileImageUrl = profile.getProfilePictureUri(120, 120).toString();
        ImageRequest imageRequest = new ImageRequest(profileImageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                navHeaderView.setImage(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyNetwork.getInstance(this).addRequest(imageRequest);
    }

    private void shareToFacebook() {
        ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
        builder.setContentUrl(Uri.parse("http://rupp.edu.kh"));
        ShareLinkContent content = builder.build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content);
    }

    private void sharePhotoToFacebook() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
        SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content);
    }

}
