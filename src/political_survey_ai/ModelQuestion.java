package political_survey_ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModelQuestion {
	private String questionId;
	private int timesWasUsed = 0;
	private HashMap<String, ModelChoice> choices = new HashMap<String, ModelChoice>();
	
	public ModelQuestion(String questionId) {
		this.questionId = questionId;
	}
	
	public String getQuestionId() {
		return questionId;
	}
	public int getTimesWasUsed() {
		return timesWasUsed;
	}
	public void setTimesWasUsed(int timesWasUsed) {
		this.timesWasUsed = timesWasUsed;
	}
	public HashMap<String, ModelChoice> getChoices() {
		return choices;
	}
	public void setChoices(HashMap<String, ModelChoice> choices) {
		this.choices = choices;
	}
	public void addChoice(String choiceIndex, String partyIndex) {
		ModelChoice choice = null;
		
		this.timesWasUsed++;

		if (choices.containsKey(choiceIndex)) {
			choice = choices.get(choiceIndex);
		} else {			
			choice = new ModelChoice(choiceIndex);
			choices.put(choiceIndex, choice);			
		}
		
		choice.add(partyIndex, this.timesWasUsed);
		
	}
	
	public String print() {
		String res = this.questionId;
		
		Iterator iter = this.choices.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String choiceIndex = (String) element.getKey();
			ModelChoice choice = (ModelChoice) element.getValue();
			res += "###" + choiceIndex + ":";
			res += choice.print();			
		}
		
		return res;
	}
	
	public void readFromText(String[] parts) {
		for (int i = 1; i < parts.length; i++) {			
			String[] choiceData = parts[i].split(":");
			String choiceIndex = choiceData[0];
			ModelChoice modelChoice = new ModelChoice(choiceIndex);
			modelChoice.readFromText(choiceData[1]);
			
			this.choices.put(choiceIndex, modelChoice);
		}
	}
}
