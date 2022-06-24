package political_survey_ai;

import java.util.ArrayList;

import gui.GUI;

public class Main {

	public static void main(String[] args) {
		GUI gui = new GUI();
		String[] parties = new String[] { "Democratic", "Republican", "Indepdendent", "Green" };
		String path = Main.class.getResource("").getPath().replace("bin", "src");
		System.out.println(path);
		String modelDataPath = path + "model_data.txt";
		String historyTestData = path + "test_data.txt";
		
		// read questions from the text file
		Questions questionsBuilder = new Questions(Main.class.getResource("questions.txt").getPath());
		ArrayList<Question> questions = questionsBuilder.getQuestions();
		
		Question lastQuestion = questionsBuilder.getLastQuestion(parties);
		
		// start survey (it's also a training session)
		gui.startSurvey(
			5,
			questions,
			historyTestData,
			modelDataPath,
			parties,
			lastQuestion
		);
		
	}

}
