package fr.difinamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.api.APIClient;
import fr.difinamic.formation.superquizz.database.QuestionDataBaseHelper;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.ui.activities.MainActivity;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnQuestionListListener}
 * interface.
 */
public class QuestionListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnQuestionListListener mListener;

    private QuestionRecyclerMemViewAdapter adapter;


    public QuestionListFragment() {}

    @SuppressWarnings("unused")
    public static QuestionListFragment newInstance(int columnCount) {
        QuestionListFragment fragment = new QuestionListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
            @Override
            public void onFailure(IOException e) {
                QuestionListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            adapter.setListQuestions(QuestionDataBaseHelper.getInstance(QuestionListFragment.this.getContext()).
                                    getAllQuestions());
                        }
                    }
                });
            }

            @Override
            public void OnSuccess(final List<Question> object) throws IOException {

                QuestionDataBaseHelper.getInstance(QuestionListFragment.this.getContext()).synchroniseDatabaseQuestions(object);

                QuestionListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            adapter.setListQuestions(object);
                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        List<Question> questions = QuestionDataBaseHelper.getInstance(getContext()).getAllQuestions();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            adapter = new QuestionRecyclerMemViewAdapter(mListener,questions);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionListListener) {
            mListener = (OnQuestionListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnQuestionListListener {
        void showQuestion(Question q);
        void updateQuestion(Question q);
        String getUserAnswer(Question q);
    }
}
