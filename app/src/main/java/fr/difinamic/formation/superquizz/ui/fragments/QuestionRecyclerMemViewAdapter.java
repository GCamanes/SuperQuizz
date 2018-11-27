package fr.difinamic.formation.superquizz.ui.fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.difinamic.formation.superquizz.R;
import fr.difinamic.formation.superquizz.model.Question;
import fr.difinamic.formation.superquizz.ui.fragments.QuestionListFragment.OnQuestionListListener;

import java.util.List;

public class QuestionRecyclerMemViewAdapter extends RecyclerView.Adapter<QuestionRecyclerMemViewAdapter.ViewHolder> {

    private List<Question> mListQuestions ;
    private final OnQuestionListListener mListener;
    private ViewGroup parent;

    public QuestionRecyclerMemViewAdapter( OnQuestionListListener listener, List<Question> questions) {
        mListener = listener;
        mListQuestions = questions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_question, parent, false);
        this.parent = parent;
        return new ViewHolder(view);
    }

    public void setListQuestions(List<Question> mListQuestions) {
        this.mListQuestions = mListQuestions;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mQuestion = mListQuestions.get(position);
        holder.mContentView.setText(String.valueOf(position+1)+ ". "+mListQuestions.get(position).getIntitule());

        Picasso.with(parent.getContext()).load("https://i.pinimg.com/236x/3b/ee/9d/3bee9dc05a93109271b7f82f151e7d32.jpg").into(holder.mImageViewUser);

        String userAnswer = mListener.getUserAnswer(holder.mQuestion);

        holder.mImageView.setBackgroundColor(Color.argb(0,0,0,0));

        if (userAnswer == null) {
            holder.mImageView.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
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
        } else {
            if (holder.mQuestion.verifierReponse(userAnswer)) {
                holder.mImageView.setImageResource(R.drawable.ic_done_black_24dp);
            } else {
                holder.mImageView.setImageResource(R.drawable.ic_close_black_24dp);
            }
            holder.mView.setOnClickListener(null);
            holder.mView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return mListQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mImageView;
        public ImageView mImageViewUser;
        public final TextView mContentView;
        public Question mQuestion;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.img_answer);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageViewUser = (ImageView) view.findViewById(R.id.img_user);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
