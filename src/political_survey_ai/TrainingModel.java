package political_survey_ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrainingModel {
	private HashMap<String, ModelQuestion> modelQuestions = new HashMap<String, ModelQuestion>();
	
	public HashMap<String, Double> currentStatistic;
	
	public TrainingModel() {
		currentStatistic = new HashMap<String, Double>();
	}
	
	public PartyAffiliation addProbabilitiesToStatistic(HashMap<String, PartyAffiliation> parties) {
		if (parties == null) {
			return null;
		}

		Iterator iter = parties.entrySet().iterator();
		
		double maxProbability = 0.0;
		PartyAffiliation maxPartyAffiliation = null;
		
		while(iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String partyIndex = (String) element.getKey();
			PartyAffiliation partyAffiliation = (PartyAffiliation) element.getValue();
			double sum = (currentStatistic.get(partyIndex) != null ? currentStatistic.get(partyIndex) : 0.0) + partyAffiliation.getProbability();
			currentStatistic.put(partyIndex, sum);
			
			if (sum > maxProbability) {
				maxProbability = sum;
				maxPartyAffiliation = partyAffiliation;
			}
		}
		
		return maxPartyAffiliation;
	}
	
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
	
	public void readFromFile(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null ) {
				String[] parts = line.split("###");
				String questionId = parts[0];
				
				ModelQuestion modelQuestion = new ModelQuestion(questionId);
				modelQuestion.readFromText(parts);				
				
				this.modelQuestions.put(questionId, modelQuestion);
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, PartyAffiliation> getAffiliationsByQuestionAndChoice(String questionId, String choiceIndex) {
		try {
			return this.modelQuestions.get(questionId).getChoices().get(choiceIndex).getPartyAffiliations();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public int getProbablePartyIndex() {
		double maxProbability = 0.0;
		int maxProbabilityPartyIndex = 0;
		Iterator iter = currentStatistic.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String currentIndex = (String) element.getKey();
			double currentProbability = (Double) element.getValue();
			
			if (currentProbability >= maxProbability) {
				maxProbability = currentProbability;
				maxProbabilityPartyIndex = Integer.parseInt(currentIndex);
			}
		}
		
		return maxProbabilityPartyIndex;
	}
}
