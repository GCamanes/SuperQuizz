package fr.difinamic.formation.superquizz.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.difinamic.formation.superquizz.R;

public class AnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final boolean answer = getIntent().getBooleanExtra("answer", false);

        if (answer) {
            ((TextView) findViewById(R.id.text_answer)).setText(getString(R.string.good_answer_message));
            ((ImageView) findViewById(R.id.img_answer)).setImageDrawable(getDrawable(R.drawable.ic_done_black_24dp));
        } else {
            ((TextView) findViewById(R.id.text_answer)).setText(getString(R.string.bad_answer_message));
            ((ImageView) findViewById(R.id.img_answer)).setImageDrawable(getDrawable(R.drawable.ic_close_black_24dp));
        }
    }

}
