package fr.difinamic.formation.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuestionMemDAO implements QuestionDAO, Parcelable {
	
	private ArrayList<Question> listQuestions = new ArrayList<Question>();
	private int scoreMax = 0;

	public QuestionMemDAO () {
		listQuestions = new ArrayList<Question>();
		scoreMax = 0;
	}

	protected QuestionMemDAO(Parcel in) {
		listQuestions = in.createTypedArrayList(Question.CREATOR);
		scoreMax = in.readInt();
	}

	public static final Creator<QuestionMemDAO> CREATOR = new Creator<QuestionMemDAO>() {
		@Override
		public QuestionMemDAO createFromParcel(Parcel in) {
			return new QuestionMemDAO(in);
		}

		@Override
		public QuestionMemDAO[] newArray(int size) {
			return new QuestionMemDAO[size];
		}
	};

	public int getScoreMax() {
		return scoreMax;
	}

	@Override
	public ArrayList<Question> findAll() {
		// TODO Auto-generated method stub
		return this.listQuestions;
	}

	@Override
	public void save(Question question) {
		// TODO Auto-generated method stub
		this.scoreMax += question.getType().getScore();
		System.out.println(question.getType() + " " + question.getType().getScore());
		this.listQuestions.add(question);
	}
	
	public boolean checkIndex(int index) {
		if (index >= 0 && index < this.listQuestions.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delete(int indexQuestion) {
		// TODO Auto-generated method stub
		this.scoreMax -= this.listQuestions.get(indexQuestion).getType().getScore();
		this.listQuestions.remove(indexQuestion);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(listQuestions);
		dest.writeInt(scoreMax);
	}
}