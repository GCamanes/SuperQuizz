package fr.difinamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.model.TypeQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnQuestionListListener}
 * interface.
 */
public class QuestionListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnQuestionListListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionListFragment() {
    }

    // TODO: Customize parameter initialization
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        List<Question> listQuestions = new ArrayList<Question>();
        Question q1 = new Question("Quelle est la capitale de la france ?", 4, TypeQuestion.SIMPLE);
        q1.addProposition("Paris");
        q1.addProposition("Rome");
        q1.addProposition("Madrid");
        q1.addProposition("Londres");
        q1.setBonneReponse("Paris");

        Question q2 = new Question("Quel héro est le plus balaise ?", 4, TypeQuestion.BONUS);
        q2.addProposition("Bob l'éponge");
        q2.addProposition("BATMAAAN !");
        q2.addProposition("Joséphine");
        q2.addProposition("Slip Man");
        q2.setBonneReponse("BATMAAAN !");

        Question q3 = new Question("Quelle est la couleur ?", 4, TypeQuestion.SIMPLE);
        q3.addProposition("Rouge");
        q3.addProposition("Bleu");
        q3.addProposition("Fushia");
        q3.addProposition("Taupe");
        q3.setBonneReponse("Bleu");


        listQuestions.add(q1);
        listQuestions.add(q2);
        listQuestions.add(q3);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new QuestionRecyclerViewAdapter(listQuestions, mListener));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnQuestionListListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Question q);
    }
}
