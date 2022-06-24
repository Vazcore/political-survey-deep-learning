package political_survey_ai;

import java.util.ArrayList;

import gui.GUI;

public class Main {

	public static void main(String[] args) {
		// Create GUI object (all visuals Forms and Buttons)
		GUI gui = new GUI();		
		String[] parties = new String[] { "Democratic", "Republican", "Indepdendent", "Green" };
		// Get path to the "src" folder
		String path = Main.class.getResource("").getPath().replace("bin", "src");
		// File path to store a model data
		String modelDataPath = path + "model_data.txt";
		// File path to store a test session data (also a history of all training sessions)
		String historyTestData = path + "test_data.txt";
		// File path to store a list of questions
		String questionsPath = path + "questions.txt";
		
		// read questions from the text file
		Questions questionsBuilder = new Questions(questionsPath);
		// read and get all questions
		ArrayList<Question> questions = questionsBuilder.getQuestions();
		
		// get last questions about a party affiliation
		Question lastQuestion = questionsBuilder.getLastQuestion(parties);
		
		TrainingModel model = null;
		
		String argsString = String.join("", args);
		boolean isTraining = false;
		// if a program was run using train=true arguments - execute a training session
		if (argsString.compareToIgnoreCase("train=true") == 0) {
			isTraining = true;
		} else {
			// load an actual model to test results
			model = new TrainingModel();
			model.readFromFile(modelDataPath);
		}
		
		// start survey (it's also a training session)
		gui.startSurvey(
		    // number of random questions
			10,
			// all questions
			questions,
			// file path to tested data
			historyTestData,
			// file path to model data
			modelDataPath,
			parties,
			lastQuestion,
			isTraining,
			model
		);		
	}

}
