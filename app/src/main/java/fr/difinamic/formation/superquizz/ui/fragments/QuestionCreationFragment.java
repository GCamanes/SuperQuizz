package fr.difinamic.formation.superquizz.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.model.TypeQuestion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionCreationFragment.OnCreatedQuestion} interface
 * to handle interaction events.
 * Use the {@link QuestionCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionCreationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUESTION = "question";

    private Question mQuestion;
    private OnCreatedQuestion mListener;

    public QuestionCreationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionCreationFragment newInstance(Question q) {
        QuestionCreationFragment fragment = new QuestionCreationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestion = getArguments().getParcelable(ARG_QUESTION );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_question_creation, container, false);

        if (mQuestion != null) {
            ((TextView) rootView.findViewById(R.id.text_question_label)).setText(mQuestion.getIntitule());
            ((TextView) rootView.findViewById(R.id.text_answer1)).setText(mQuestion.getPropositions().get(0));
            ((TextView) rootView.findViewById(R.id.text_answer2)).setText(mQuestion.getPropositions().get(1));
            ((TextView) rootView.findViewById(R.id.text_answer3)).setText(mQuestion.getPropositions().get(2));
            ((TextView) rootView.findViewById(R.id.text_answer4)).setText(mQuestion.getPropositions().get(3));

            if (mQuestion.verifierReponse(mQuestion.getPropositions().get(0))) {
                ((RadioButton) rootView.findViewById(R.id.radio_answer1)).setChecked(true);
            } else if (mQuestion.verifierReponse(mQuestion.getPropositions().get(1))) {
                ((RadioButton) rootView.findViewById(R.id.radio_answer2)).setChecked(true);
            } else if(mQuestion.verifierReponse(mQuestion.getPropositions().get(2))) {
                ((RadioButton) rootView.findViewById(R.id.radio_answer3)).setChecked(true);
            } else {
                ((RadioButton) rootView.findViewById(R.id.radio_answer4)).setChecked(true);
            }
        } else {
            ((RadioButton) rootView.findViewById(R.id.radio_answer1)).setChecked(true);
        }

        RadioButton.OnCheckedChangeListener onCheckedChangeListener = new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((RadioButton) rootView.findViewById(R.id.radio_answer1)).setChecked(false);
                    ((RadioButton) rootView.findViewById(R.id.radio_answer2)).setChecked(false);
                    ((RadioButton) rootView.findViewById(R.id.radio_answer3)).setChecked(false);
                    ((RadioButton) rootView.findViewById(R.id.radio_answer4)).setChecked(false);

                    ((RadioButton) buttonView).setChecked(true);
                }
            }
        };

        ((RadioButton) rootView.findViewById(R.id.radio_answer1)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) rootView.findViewById(R.id.radio_answer2)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) rootView.findViewById(R.id.radio_answer3)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) rootView.findViewById(R.id.radio_answer4)).setOnCheckedChangeListener(onCheckedChangeListener);

        rootView.findViewById(R.id.button_add_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFullyFilled()) {
                    if (mQuestion == null) {
                        Question q = new Question(((TextView) getView().findViewById(R.id.text_question_label)).getText().toString(), 4, TypeQuestion.SIMPLE);
                        q.setBonneReponse(getGoodAnswer());
                        q.addProposition(((TextView) getView().findViewById(R.id.text_answer1)).getText().toString());
                        q.addProposition(((TextView) getView().findViewById(R.id.text_answer2)).getText().toString());
                        q.addProposition(((TextView) getView().findViewById(R.id.text_answer3)).getText().toString());
                        q.addProposition(((TextView) getView().findViewById(R.id.text_answer4)).getText().toString());
                        mListener.saveQuestion(q);
                    } else {
                        mQuestion.setIntitule(((TextView) getView().findViewById(R.id.text_question_label)).getText().toString());
                        mQuestion.setBonneReponse(getGoodAnswer());
                        mQuestion.getPropositions().set(0,(((TextView) getView().findViewById(R.id.text_answer1)).getText().toString()));
                        mQuestion.getPropositions().set(1,(((TextView) getView().findViewById(R.id.text_answer2)).getText().toString()));
                        mQuestion.getPropositions().set(2,(((TextView) getView().findViewById(R.id.text_answer3)).getText().toString()));
                        mQuestion.getPropositions().set(3,(((TextView) getView().findViewById(R.id.text_answer4)).getText().toString()));
                        mListener.saveQuestion(mQuestion);
                    }
                } else {
                    Toast.makeText(getContext(), "Il faut remplir tous les champs pour sauvegarder la question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void setListener(OnCreatedQuestion mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean isFullyFilled() {
        if (((TextView) getView().findViewById(R.id.text_question_label)).getText().toString().isEmpty() ||
                ((TextView) getView().findViewById(R.id.text_answer1)).getText().toString().isEmpty() ||
                ((TextView) getView().findViewById(R.id.text_answer2)).getText().toString().isEmpty() ||
                ((TextView) getView().findViewById(R.id.text_answer3)).getText().toString().isEmpty() ||
                ((TextView) getView().findViewById(R.id.text_answer3)).getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private String getGoodAnswer() {

        if (((RadioButton) getView().findViewById(R.id.radio_answer1)).isChecked()) {
            return ((TextView) getView().findViewById(R.id.text_answer1)).getText().toString();
        } else if (((RadioButton) getView().findViewById(R.id.radio_answer2)).isChecked()) {
            return ((TextView) getView().findViewById(R.id.text_answer2)).getText().toString();
        } else if (((RadioButton) getView().findViewById(R.id.radio_answer3)).isChecked()) {
            return ((TextView) getView().findViewById(R.id.text_answer3)).getText().toString();
        } else {
            return ((TextView) getView().findViewById(R.id.text_answer4)).getText().toString();
        }
    }


    public interface OnCreatedQuestion {
        void saveQuestion(Question q);
    }
}
