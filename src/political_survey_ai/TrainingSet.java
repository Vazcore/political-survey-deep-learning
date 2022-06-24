package political_survey_ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TrainingSet {
	private String party;	
	private ArrayList<TrainingQuestion> questions = new ArrayList<TrainingQuestion>();
	private String testDataPath;
	private String modelDataPath;
	
	public TrainingSet(String testDataPath, String modelDataPath) {
		this.testDataPath = testDataPath;
		this.modelDataPath = modelDataPath;
	}
	
	public String getModelDataPath() {
		return modelDataPath;
	}

	public void add(TrainingQuestion q) {
		this.questions.add(q);
	}
	
	public void setParty(String party) {
		this.party = party;
	}
	
	public String getParty() {
		return this.party;
	}
	
	public ArrayList<TrainingQuestion> getQuestions() {
		return this.questions;
	}
	
	public void writeHistory() {
		String res = "";
		res += this.party;
		
		ArrayList<TrainingQuestion> recordedQuestions = (ArrayList<TrainingQuestion>) this.questions.clone();
		recordedQuestions.remove(recordedQuestions.size() - 1);
		
		Iterator<TrainingQuestion> iter = recordedQuestions.iterator();
		while(iter.hasNext()) {
			TrainingQuestion tq = iter.next();
			res += "###";
			res += tq.getQuestion() + "," + tq.getAnswer();
		}
		
		this.write(this.testDataPath, res, true);
	}
	
	private void write(String path, String data, boolean isAppend) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, isAppend == true);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.write(data);
			
			if (isAppend) {
				writer.newLine();
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void trainModel() {
		TrainingModel model = new TrainingModel();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.testDataPath));
			String line = reader.readLine();
			while (line != null ) {
				String[] parts = line.split("###");
				String party = parts[0];
				
				for (int i = 1; i < parts.length; i++) {
					String[] choices = parts[i].split(",");
					String questionId = choices[0];
					String choiceIndex = choices[1];
					model.addChoice(questionId, choiceIndex, party);					
				}
				
				line = reader.readLine();
			}
			reader.close();
			
			String data = model.getPrintableData();
			this.write(this.modelDataPath, data, false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readTrainingModel() {
		TrainingModel model = new TrainingModel();
		model.readFromFile(this.modelDataPath);
	}
}
