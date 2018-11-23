package fr.difinamic.formation.superquizz.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.difinamic.formation.superquizz.database.QuestionDataBaseHelper;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.model.TypeQuestion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance;

    public static APIClient getInstance(){
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request request = new Request.Builder()
                .url("http://192.168.10.38:3000/questions")
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
                                j.getInt("id"),
                                j.getString("title"),
                                4, TypeQuestion.SIMPLE);

                        q.addProposition(j.getString("answer_1"));
                        q.addProposition(j.getString("answer_2"));
                        q.addProposition(j.getString("answer_3"));
                        q.addProposition(j.getString("answer_4"));

                        q.setBonneReponse(q.getPropositions().get(Integer.parseInt(j.getString("correct_answer"))-1));

                        questions.add(q);
                    }

                } catch (JSONException e) {}

                result.OnSuccess(questions);
            }
        });
    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }
}