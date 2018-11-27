package fr.difinamic.formation.superquizz.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import fr.difinamic.formation.superquizz.R;

public class InfosActivity extends AppCompatActivity {

    private CheckBox checkSaveUserAnswers;

    private static final String SHARED_PREF_KEEP_ANSWERS = "settings_keepUserAnswersOnRefresh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkSaveUserAnswers = findViewById(R.id.checkbox_keep_answer_on_refresh);

        checkSaveUserAnswers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(InfosActivity.this);

                SharedPreferences.Editor editor = mSettings.edit();

                boolean saveUserAnswers = buttonView.isChecked();
                editor.putBoolean(SHARED_PREF_KEEP_ANSWERS, saveUserAnswers);

                editor.apply();
            }
        });

        checkSaveUserAnswers.setChecked(
                PreferenceManager.getDefaultSharedPreferences(InfosActivity.this).
                    getBoolean(SHARED_PREF_KEEP_ANSWERS, false));

    }
}
