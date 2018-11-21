package fr.difinamic.formation.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuestionMemDAO implements QuestionDAO {
	
	private ArrayList<Question> listQuestions = new ArrayList<Question>();
	private int scoreMax = 0;

	private static QuestionMemDAO sInstance;


	public static QuestionMemDAO getInstance(){

		if( sInstance == null) {
			sInstance = new QuestionMemDAO();

			// Demo Values for display memory DAO
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


			sInstance.save(q1);
			sInstance.save(q2);
			sInstance.save(q3);
		}

		return sInstance;

	}

	protected QuestionMemDAO () {
		listQuestions = new ArrayList<Question>();
		scoreMax = 0;
	}



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

}