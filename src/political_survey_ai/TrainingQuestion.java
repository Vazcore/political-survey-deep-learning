package political_survey_ai;

public class TrainingQuestion {
	private String question;
	private String answer;
	
	public TrainingQuestion(String q, String a) {
		this.question = q;
		this.answer = a;
	}
	
	public String getQuestion() {
		return question;
	}
	public String getAnswer() {
		return answer;
	}
}
