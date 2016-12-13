package kh.edu.rupp.ckcc.eclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

        toolbar = (Toolbar)findViewById(R.id.toolbar);
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

        navHeaderView.setUsername("CKCC");
        navHeaderView.setImage(R.drawable.ic_profile);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onEditClick() {
        Toast.makeText(this, "on edit click", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogoutClick() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Toast.makeText(this, "on menu 1 click", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu2:
                Toast.makeText(this, "on menu 2 click", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu3:
                Toast.makeText(this, "on menu 3 click", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu4:
                Toast.makeText(this, "on menu 4 click", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu5:
                Toast.makeText(this, "on menu 5 click", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
}
