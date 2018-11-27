package fr.difinamic.formation.superquizz.ui.threads;

import android.os.AsyncTask;
import android.os.SystemClock;

public class AnswerQuestionTask extends AsyncTask<Void, Integer, String> {
    int count = 0;

    private OnAnswerQuestionListener mListener;

    public AnswerQuestionTask(OnAnswerQuestionListener listener) {
        super();
        this.mListener = listener;
    }

    public int getElapsedTime() {
        return count*10;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... params) {
        while (count < 50) {
            if (isCancelled()) {
                break;
            } else {
                SystemClock.sleep(100);
                count++;
                publishProgress(count * 2);
            }
        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mListener.onProgressTaskAnswerQuestion(count*2);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mListener.onCompletedTaskAnswerQuestion();
    }

    public interface OnAnswerQuestionListener {
        void onProgressTaskAnswerQuestion(int progress);
        void onCompletedTaskAnswerQuestion();
    }
}
