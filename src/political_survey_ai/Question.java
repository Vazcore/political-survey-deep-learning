package political_survey_ai;

import java.util.ArrayList;

public class Question {
	private int id;
	private String question;
	private ArrayList<String> answerChoices;
	
	public Question(int id, String question, ArrayList<String> answers) {
		this.id = id;
		this.question = question;
		this.answerChoices = answers;
	}	

	public int getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}

	public ArrayList<String> getAnswerChoices() {
		return answerChoices;
	}
}
