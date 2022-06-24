package political_survey_ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Questions {
	private ArrayList<Question> questions;
	
	public Questions(String filepath) {
		questions = this.readFromFile(filepath);
	}
	
	public Question getLastQuestion(String[] parties) {
		return new Question(-1, "Which political party do you affiliate with?", new ArrayList<String>(Arrays.asList(parties)));
	}
	
	private ArrayList<Question> readFromFile(String filepath) {
		ArrayList<Question> questions = new ArrayList<Question>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			while (line != null ) {
				String[] dataParts = line.split("###");
				int id = Integer.parseInt(dataParts[0]);
				String question = dataParts[1];
				
				ArrayList<String> answerChoices = new ArrayList<String>();
				for(int i = 2; i < dataParts.length; i++) {
					answerChoices.add(dataParts[i]);
				}
				
				questions.add(new Question(id, question, answerChoices));
				
				line = reader.readLine();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return questions;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}
}
