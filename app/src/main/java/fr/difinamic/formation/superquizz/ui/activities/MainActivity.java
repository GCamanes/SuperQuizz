package fr.difinamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.api.APIClient;
import fr.difinamic.formation.superquizz.database.QuestionDataBaseHelper;
import fr.difinamic.formation.superquizz.model.*;
import fr.difinamic.formation.superquizz.ui.fragments.HomeFragment;
import fr.difinamic.formation.superquizz.ui.fragments.QuestionCreationFragment;
import fr.difinamic.formation.superquizz.ui.fragments.QuestionListFragment;
import fr.difinamic.formation.superquizz.ui.fragments.ScoreFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionListFragment.OnQuestionListListener,
        QuestionCreationFragment.OnCreatedQuestion, QuestionActivity.OnAnswerSelectedListener {

    private static final String ARG_QUESTION = "question";

    private static Fragment currentFragment;
    private static final String ARG_CURRENTFRAGMENT = "current_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
            @Override
            public void onFailure(IOException e) {
                Toast.makeText(MainActivity.this, "ERROR WITH HTTP SERVEUR", Toast.LENGTH_SHORT);
            }

            @Override
            public void OnSuccess(List<Question> object) throws IOException {
                for (Question q : object) {
                    //QuestionDataBaseHelper.getInstance(MainActivity.this).addQuestion(q);
                    QuestionDataBaseHelper.getInstance(MainActivity.this).addOrUpdateQuestion(q);
                }
            }
        });*/

        if(currentFragment == null) {
            displayHomeFragment();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, currentFragment).commit();
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayHomeFragment();
        } else if (id == R.id.nav_play) {
           displayQuestionListFragment();
        } else if (id == R.id.nav_score) {
            displayScoreFragment();
        } else if (id == R.id.nav_add_question) {
            displayQuestionCreationFragment(null);
        }else if (id == R.id.nav_infos) {
            displayInfosActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayHomeFragment() {
        HomeFragment fragment = HomeFragment.newInstance(getString(R.string.app_name));
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
    }

    public void displayQuestionListFragment(){
        QuestionListFragment fragment = QuestionListFragment.newInstance(1);
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
    }

    public void displayScoreFragment() {
        ScoreFragment fragment = ScoreFragment.newInstance(3, 4);
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
    }

    public void displayQuestionCreationFragment(Question q) {
        QuestionCreationFragment fragment = QuestionCreationFragment.newInstance(q);
        currentFragment = fragment;
        fragment.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frament_container, fragment).commit();
    }

    public void displayInfosActivity() {
        Intent intentInfo = new Intent(MainActivity.this, InfosActivity.class);
        startActivity(intentInfo);
    }

    // Function save data (called when onCreate() is called)
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showQuestion(Question q) {
        Intent intentQuestion = new Intent(MainActivity.this, QuestionActivity.class);
        intentQuestion.putExtra(ARG_QUESTION, q);
        startActivity(intentQuestion);
    }

    @Override
    public void updateQuestion(Question q) {
        displayQuestionCreationFragment(q);
    }

    @Override
    public void saveQuestion(Question q) {
        if (q.getId() == -1) {
            QuestionDataBaseHelper.getInstance(this).addQuestion(q);
        } else {
            QuestionDataBaseHelper.getInstance(this).updateQuestion(q);
        }

        displayQuestionListFragment();
    }

    @Override
    public void saveUserAnswer(boolean answer) {

    }
}
