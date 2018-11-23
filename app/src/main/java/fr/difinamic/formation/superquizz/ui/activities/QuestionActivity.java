package fr.difinamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.database.QuestionDataBaseHelper;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.ui.threads.AnswerQuestionTask;

public class QuestionActivity extends AppCompatActivity implements AnswerQuestionTask.OnAnswerQuestionListener {

    private AnswerQuestionTask answerQuestionTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Question question = getIntent().getParcelableExtra("question");

        TextView textIntitule = (TextView)findViewById(R.id.text_intitule);
        textIntitule.setText(question.getIntitule());

        Button button1 = (Button) findViewById(R.id.button_rep1);
        button1.setText(question.getPropositions().get(0));
        Button button2 = (Button) findViewById(R.id.button_rep2);
        button2.setText(question.getPropositions().get(1));
        Button button3 = (Button) findViewById(R.id.button_rep3);
        button3.setText(question.getPropositions().get(2));
        Button button4 = (Button) findViewById(R.id.button_rep4);
        button4.setText(question.getPropositions().get(3));

        this.answerQuestionTask = new AnswerQuestionTask(this);

        View.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = question.verifierReponse(((Button )v).getText().toString());
                Intent intentAnswer = new Intent(QuestionActivity.this, AnswerActivity.class );
                intentAnswer.putExtra("answer", check);
                startActivity(intentAnswer);
                answerQuestionTask.cancel(true);
            }
        };

        button1.setOnClickListener(checkListener);
        button2.setOnClickListener(checkListener);
        button3.setOnClickListener(checkListener);
        button4.setOnClickListener(checkListener);


        answerQuestionTask.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        answerQuestionTask.cancel(true);
    }

    public void onProgressTaskAnswerQuestion(int progress) {
        ((ProgressBar) findViewById(R.id.progress_question)).setProgress(progress);
    }
    public void onCompletedTaskAnswerQuestion() {
        Intent intentAnswer = new Intent(QuestionActivity.this, AnswerActivity.class);
        intentAnswer.putExtra("answer", false);
        startActivity(intentAnswer);
    }
}


