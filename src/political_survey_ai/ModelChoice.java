package political_survey_ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ModelChoice {
	private int choiceIndex;
	private HashMap<String, PartyAffiliation> partyAffiliations;
	
	public ModelChoice(String choiceIndex) {
		this.choiceIndex = Integer.parseInt(choiceIndex);
		this.partyAffiliations = new HashMap<String, PartyAffiliation>();
	}

	public int getChoiceIndex() {
		return choiceIndex;
	}
	
	public HashMap<String, PartyAffiliation> getPartyAffiliations() {
		return this.partyAffiliations;
	}

	public PartyAffiliation getProbabilityBypartyIndex(String partyIndex) {
		return partyAffiliations.get(partyIndex);
	}
	
	public void add(String partyIndex, int timesQuestionWasUsed) {
		PartyAffiliation partyAffiliation;
		if (this.partyAffiliations.containsKey(partyIndex)) {
			partyAffiliation = this.partyAffiliations.get(partyIndex);
		} else {
			partyAffiliation = new PartyAffiliation();
			partyAffiliation.setPartyIndex(Integer.parseInt(partyIndex));
			this.partyAffiliations.put(partyIndex, partyAffiliation);
		}
		partyAffiliation.setTimesWasUsed(partyAffiliation.getTimesWasUsed() + 1);
		//partyAffiliation.setProbability((double) partyAffiliation.getTimesWasUsed() / (double) timesQuestionWasUsed);
		this.recaculateAllProbabilities(timesQuestionWasUsed);
	}
	
	private void recaculateAllProbabilities(int timesQuestionWasUsed) {
		Iterator iter = this.partyAffiliations.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String partyIndex = (String) element.getKey();
			PartyAffiliation affiliation = (PartyAffiliation) element.getValue();
			double probability = (double) affiliation.getTimesWasUsed() / (double) timesQuestionWasUsed;
			affiliation.setProbability(probability);
			element.setValue(affiliation);
		}
	}

	public String print() {
		String res = "";
		
		ArrayList<String> parties = new ArrayList<String>();
		
		Iterator iter = this.partyAffiliations.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry element = (Map.Entry) iter.next();
			String partyIndex = (String) element.getKey();
			PartyAffiliation affiliation = (PartyAffiliation) element.getValue();
			parties.add(partyIndex + "-" + affiliation.getProbability());
		}
		
		res += String.join(",", parties);
		
		return res;
	}
	
	public void readFromText(String choiceData) {
		String[] affiliationsData = choiceData.split(",");
		for (int i = 0; i < affiliationsData.length; i++) {
			PartyAffiliation affiliation = new PartyAffiliation();
			String[] values = affiliationsData[i].split("-");
			affiliation.readFromText(values);
			this.partyAffiliations.put(values[0], affiliation);
		}
	}
	
}
