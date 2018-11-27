package fr.difinamic.formation.superquizz.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.model.TypeQuestion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();

    //private final String serverUrl = "http://192.168.10.38:3000/";
    private final String serverUrl = "http://192.168.10.48:3000/";

    private static APIClient sInstance;


    private static final String KEY_SERVER_ID = "id";
    private static final String KEY_SERVER_LABEL = "title";
    private static final String KEY_SERVER_ANSWER1 = "answer_1";
    private static final String KEY_SERVER_ANSWER2 = "answer_2";
    private static final String KEY_SERVER_ANSWER3 = "answer_3";
    private static final String KEY_SERVER_ANSWER4 = "answer_4";
    private static final String KEY_SERVER_GOOD_ANSWER = "correct_answer";
    private static final String KEY_SERVER_AUTHOR_IMG_URL = "author_img_url";
    private static final String KEY_SERVER_AUTHOR = "author";

    public static APIClient getInstance(){
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request request = new Request.Builder()
                .url(serverUrl+"questions")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Question> questions = new ArrayList<>();

                try {
                    String responseData = response.body().string();
                    JSONArray json = new JSONArray(responseData);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject j = json.getJSONObject(i);
                        Question q = new Question(
                                j.getInt(KEY_SERVER_ID),
                                j.getString(KEY_SERVER_LABEL),
                                4, TypeQuestion.SIMPLE);

                        q.addProposition(j.getString(KEY_SERVER_ANSWER1));
                        q.addProposition(j.getString(KEY_SERVER_ANSWER2));
                        q.addProposition(j.getString(KEY_SERVER_ANSWER3));
                        q.addProposition(j.getString(KEY_SERVER_ANSWER4));

                        q.setBonneReponse(q.getPropositions().get(Integer.parseInt(j.getString(KEY_SERVER_GOOD_ANSWER))-1));

                        questions.add(q);
                    }

                } catch (JSONException e) {}

                result.OnSuccess(questions);
            }
        });
    }

    public void createQuestion(final APIResult<Question> result, final Question q) {

        // Prepare body for post method
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json = parseQuestionToJSON(q);
        } catch (JSONException e) { }

        Request request = new Request.Builder()
                .url(serverUrl+"questions").method("POST", RequestBody.create(JSON_TYPE,json.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
                Log.i("SAVE QUESTION", "Question saved");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result.OnSuccess(q);
                Log.i("SAVE QUESTION", "Question not saved");
            }
        });
    }

    public void updateQuestion(final APIResult<Question> result, final Question q) {

        // Prepare body for post method
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json = parseQuestionToJSON(q);
        } catch (JSONException e) {

        }

        Request request = new Request.Builder()
                .url(serverUrl+"questions/"+q.getId()).method("PUT", RequestBody.create(JSON_TYPE,json.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
                Log.i("UPDATE QUESTION", "Question updated");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result.OnSuccess(q);
                Log.i("UPDATE QUESTION", "Question not updated");
            }
        });
    }

    private JSONObject parseQuestionToJSON(Question q) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_SERVER_GOOD_ANSWER,(q.getPropositions().indexOf(q.getBonneReponse())+1));
        json.put(KEY_SERVER_ANSWER1,q.getPropositions().get(0));
        json.put(KEY_SERVER_ANSWER2,q.getPropositions().get(1));
        json.put(KEY_SERVER_ANSWER3,q.getPropositions().get(2));
        json.put(KEY_SERVER_ANSWER4,q.getPropositions().get(3));
        json.put(KEY_SERVER_LABEL, q.getIntitule());
        return json;
    }

    private Question parseJsonObject (JSONObject jsonObject) throws JSONException {


        Question question = new Question(
                jsonObject.getInt(KEY_SERVER_ID),
                jsonObject.getString(KEY_SERVER_LABEL),
                4, TypeQuestion.SIMPLE);

        question.addProposition(jsonObject.getString(KEY_SERVER_ANSWER1));
        question.addProposition(jsonObject.getString(KEY_SERVER_ANSWER2));
        question.addProposition(jsonObject.getString(KEY_SERVER_ANSWER3));
        question.addProposition(jsonObject.getString(KEY_SERVER_ANSWER4));

        question.setBonneReponse(question.getPropositions().get(Integer.parseInt(jsonObject.getString(KEY_SERVER_GOOD_ANSWER))-1));

        return question;

    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}