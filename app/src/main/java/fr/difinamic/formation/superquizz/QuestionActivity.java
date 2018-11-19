package fr.difinamic.formation.superquizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.difinamic.formation.superquizz.model.Question;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Question question = getIntent().getParcelableExtra("question");

        TextView textIntitule = (TextView)findViewById(R.id.textIntitule);
        textIntitule.setText(question.getIntitule());

        Button button1 = (Button) findViewById(R.id.button_rep1);
        button1.setText(question.getPropositions().get(0));
        Button button2 = (Button) findViewById(R.id.button_rep2);
        button2.setText(question.getPropositions().get(1));
        Button button3 = (Button) findViewById(R.id.button_rep3);
        button3.setText(question.getPropositions().get(2));
        Button button4 = (Button) findViewById(R.id.button_rep4);
        button4.setText(question.getPropositions().get(3));

        View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = question.verifierReponse(((Button )v).getText().toString());
                Intent intentAnswer = new Intent(QuestionActivity.this, AnswerActivity.class );
                intentAnswer.putExtra("answer", check);
                startActivity(intentAnswer);
            }
        };

        button1.setOnClickListener(checkListener);
        button2.setOnClickListener(checkListener);
        button3.setOnClickListener(checkListener);
        button4.setOnClickListener(checkListener);
    }
}
