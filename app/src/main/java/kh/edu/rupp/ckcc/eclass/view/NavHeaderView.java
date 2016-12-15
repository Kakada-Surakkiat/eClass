package kh.edu.rupp.ckcc.eclass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 8/12/16.
 */

public class NavHeaderView extends RelativeLayout implements View.OnClickListener {

    private CircleImageView imgProfile;
    private TextView txtUsername;
    private OnNavHeaderItemClick onNavHeaderItemClick;

    public NavHeaderView(Context context) {
        super(context);

        initViews(context);
    }

    public NavHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initViews(context);
    }

    public NavHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(context);
    }

    public void setOnNavHeaderItemClick(OnNavHeaderItemClick onNavHeaderItemClick) {
        this.onNavHeaderItemClick = onNavHeaderItemClick;
    }

    private void initViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.nav_header, this);
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        txtUsername = (TextView) findViewById(R.id.txt_username);
        findViewById(R.id.txt_view).setOnClickListener(this);
        findViewById(R.id.txt_logout).setOnClickListener(this);
    }

    public void setUsername(String username) {
        txtUsername.setText(username);
    }

    public void setImage(@DrawableRes int imageResId) {
        imgProfile.setImageResource(imageResId);
    }

    public void setImage(Bitmap bitmap) {
        imgProfile.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view) {
            if (onNavHeaderItemClick != null) {
                onNavHeaderItemClick.onViewClick();
            }
        } else if (v.getId() == R.id.txt_logout) {
            if (onNavHeaderItemClick != null) {
                onNavHeaderItemClick.onLogoutClick();
            }
        }
    }

    public interface OnNavHeaderItemClick {
        void onViewClick();

        void onLogoutClick();
    }

}
