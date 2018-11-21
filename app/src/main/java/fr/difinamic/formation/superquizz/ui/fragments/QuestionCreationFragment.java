package fr.difinamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCreatedQuestion mListener;

    public QuestionCreationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionCreationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionCreationFragment newInstance(String param1, String param2) {
        QuestionCreationFragment fragment = new QuestionCreationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_question_creation, container, false);

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

                //TODO get real value from the interface
                Question q = new Question("TEST", 4,TypeQuestion.BONUS);
                q.setBonneReponse("tttttt");
                q.addProposition("tttttt");
                q.addProposition("ze");
                q.addProposition("ze");
                q.addProposition("ze");
                mListener.saveQuestion(q);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCreatedQuestion {
        // TODO: Update argument type and name
        void saveQuestion(Question q);
    }
}
