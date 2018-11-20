package fr.difinamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.difinamic.formation.superquizz.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SCORE = "score";
    private static final String ARG_SCOREMAX = "scoreMax";

    // TODO: Rename and change types of parameters
    private Integer mScore;
    private Integer mScoreMax;

    public ScoreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ScoreFragment newInstance(int score, int scoreMax) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_SCOREMAX, scoreMax);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScore = getArguments().getInt(ARG_SCORE);
            mScoreMax = getArguments().getInt(ARG_SCOREMAX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score, container, false);

        ((TextView)rootView.findViewById(R.id.text_score)).setText(mScore.toString());
        ((TextView)rootView.findViewById(R.id.text_score_max)).setText(mScoreMax.toString());

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
