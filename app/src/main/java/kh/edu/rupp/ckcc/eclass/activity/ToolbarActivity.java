package kh.edu.rupp.ckcc.eclass.activity;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 5/1/17.
 */

public class ToolbarActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setToolbarTitle(String title){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar == null){
            return;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setToolbarTitle(@StringRes int stringId){
        setToolbarTitle(getString(stringId));
    }

    protected void showLongToast(@StringRes int stringId){
        Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();
    }


}
