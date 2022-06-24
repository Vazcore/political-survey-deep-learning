package political_survey_ai;

import java.util.ArrayList;

import gui.GUI;

public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
		String[] parties = new String[] { "Democratic", "Republican", "Indepdendent", "Green" };
		String path = Main.class.getResource("").getPath().replace("bin", "src");
		String modelDataPath = path + "model_data.txt";
		String historyTestData = path + "test_data.txt";
		String questionsPath = path + "questions.txt";
		
		// read questions from the text file
		Questions questionsBuilder = new Questions(questionsPath);
		ArrayList<Question> questions = questionsBuilder.getQuestions();
		
		Question lastQuestion = questionsBuilder.getLastQuestion(parties);
		
		TrainingModel model = null;
		
		String argsString = String.join("", args);
		boolean isTraining = false;
		if (argsString.compareToIgnoreCase("train=true") == 0) {
			isTraining = true;
		} else {
			model = new TrainingModel();
			model.readFromFile(modelDataPath);
		}
		
		// start survey (it's also a training session)
		gui.startSurvey(
			10,
			questions,
			historyTestData,
			modelDataPath,
			parties,
			lastQuestion,
			isTraining,
			model
		);		
	}

}
