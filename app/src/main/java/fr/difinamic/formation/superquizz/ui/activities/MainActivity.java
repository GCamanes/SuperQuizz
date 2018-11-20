package fr.difinamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.model.*;
import fr.difinamic.formation.superquizz.ui.fragments.HomeFragment;
import fr.difinamic.formation.superquizz.ui.fragments.PlayFragment;
import fr.difinamic.formation.superquizz.ui.fragments.QuestionListFragment;
import fr.difinamic.formation.superquizz.ui.fragments.ScoreFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayFragment.OnFragmentInteractionListener, QuestionListFragment.OnQuestionListListener,
        HomeFragment.OnFragmentInteractionListener {

    private static final String ARG_QUESTION = "question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            HomeFragment fragment = HomeFragment.newInstance(getString(R.string.app_name));
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment fragment = HomeFragment.newInstance(getString(R.string.app_name));
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
        } else if (id == R.id.nav_play) {
            QuestionListFragment fragment = QuestionListFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
        } else if (id == R.id.nav_score) {
            ScoreFragment fragment = ScoreFragment.newInstance(3, 4);
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
        } else if (id == R.id.nav_infos) {
            Intent intentInfo = new Intent(MainActivity.this, InfosActivity.class);
            startActivity(intentInfo);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Function save data (called when onCreate() is called)
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onListFragmentInteraction(Question q) {
        Intent intentQuestion = new Intent(MainActivity.this, QuestionActivity.class);
        intentQuestion.putExtra(ARG_QUESTION, q);
        startActivity(intentQuestion);
    }
}
