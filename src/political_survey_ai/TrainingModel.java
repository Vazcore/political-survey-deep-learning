package political_survey_ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrainingModel {
	private HashMap<String, ModelQuestion> modelQuestions = new HashMap<String, ModelQuestion>();
	
	public void addChoice(String questionId, String choiceIndex, String partyIndex) {
		ModelQuestion modelQuestion;
		if (modelQuestions.containsKey(questionId)) {
			modelQuestion = this.modelQuestions.get(questionId);
		} else {
			modelQuestion = new ModelQuestion(questionId);
			this.modelQuestions.put(questionId, modelQuestion);
		}
		
		modelQuestion.addChoice(choiceIndex, partyIndex);
	}
	
	public String getPrintableData() {
		String res = "";
		
		Iterator iter = this.modelQuestions.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String questionId = (String) element.getKey();
			ModelQuestion question = (ModelQuestion) element.getValue();
			res += question.print();
			res += "\n";
		}
		
		return res;
	}
}
