package fr.difinamic.formation.superquizz.model;

public enum TypeQuestion {

	SIMPLE("simple", 1),
	BONUS("bonus", 2);
	
	private String type;
	
	private int score;
	
	public int getScore() {
		return score;
	}

	private TypeQuestion(String type, int score) {
		this.type = type;
		this.score = score;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
		
	public static TypeQuestion getByType(String type) {
		TypeQuestion[] tqs = values();
		for (TypeQuestion tq: tqs){
			if (tq.getType().equals(type)) {
				return tq;
			}
		}
		return null;
	}
}
