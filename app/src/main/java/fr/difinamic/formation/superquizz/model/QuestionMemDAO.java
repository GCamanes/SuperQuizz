package fr.difinamic.formation.superquizz.model;

import java.util.ArrayList;

public class QuestionMemDAO implements QuestionDAO {
	
	private ArrayList<Question> listQuestions = new ArrayList<Question>();
	private int scoreMax = 0;
	
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