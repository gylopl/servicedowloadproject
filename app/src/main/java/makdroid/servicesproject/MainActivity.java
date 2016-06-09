package makdroid.servicesproject;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import makdroid.servicesproject.fragments.BoundServiceFragment;
import makdroid.servicesproject.fragments.FileModelFragment;
import makdroid.servicesproject.fragments.IntentServiceFragment;
import makdroid.servicesproject.fragments.ServiceFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment = null;
    private String fragment_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
            setFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_service_download:
                fragmentClass = ServiceFragment.class;
                fragment_tag = ServiceFragment.FRAGMENT_TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(fragment_tag);
                break;
            case R.id.nav_intent_service_download:
                fragmentClass = IntentServiceFragment.class;
                fragment_tag = IntentServiceFragment.FRAGMENT_TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(fragment_tag);
                break;
            case R.id.nav_bound_service:
                fragmentClass = BoundServiceFragment.class;
                fragment_tag = BoundServiceFragment.FRAGMENT_TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(fragment_tag);
                break;
            case R.id.nav_files:
                fragmentClass = FileModelFragment.class;
                fragment_tag = FileModelFragment.FRAGMENT_TAG;
                fragment = getSupportFragmentManager().findFragmentByTag(fragment_tag);
                break;
            default:
                fragmentClass = IntentServiceFragment.class;
                break;
        }

        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setFragment() {
        if (fragment == null) {
            fragment = new ServiceFragment();
            fragment_tag = ServiceFragment.FRAGMENT_TAG;
        }
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager
                .replace(R.id.nav_contentframe, fragment, fragment_tag);
        fragmentManager.addToBackStack(null);
        fragmentManager.commit();
    }
}
