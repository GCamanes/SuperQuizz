package fr.difinamic.formation.superquizz.ui.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.ui.activities.MainActivity;
import fr.difinamic.formation.superquizz.ui.fragments.QuestionListFragment.OnQuestionListListener;

import java.util.List;

public class QuestionRecyclerMemViewAdapter extends RecyclerView.Adapter<QuestionRecyclerMemViewAdapter.ViewHolder> {

    private List<Question> mListQuestions ;
    private final OnQuestionListListener mListener;

    public QuestionRecyclerMemViewAdapter( OnQuestionListListener listener, List<Question> questions) {
        mListener = listener;
        mListQuestions = questions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mQuestion = mListQuestions.get(position);
        holder.mIdView.setText(String.valueOf(position+1));
        holder.mContentView.setText(mListQuestions.get(position).getIntitule());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.showQuestion(holder.mQuestion);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.updateQuestion(holder.mQuestion);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Question mQuestion;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
