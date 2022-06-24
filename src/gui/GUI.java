package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;

import political_survey_ai.PartyAffiliation;
import political_survey_ai.Question;
import political_survey_ai.TrainingModel;
import political_survey_ai.TrainingQuestion;
import political_survey_ai.TrainingSet;

import javax.swing.JLabel;
import javax.swing.JButton;


public class GUI {
	public GUI() {
		
	}
	
	public void startSurvey(
		int numOfQuestions,
		ArrayList<Question> questions,
		String testDataPath,
		String modelDataPath,
		String[] parties,
		Question lastQuestion,
		boolean isTraining,
		TrainingModel model
	) {
		ArrayList<Question> randomQuestions = getRandomQuestions(numOfQuestions, questions);
		TrainingSet trainingSet = null;

		if (isTraining) {
			randomQuestions.add(lastQuestion);
			trainingSet = new TrainingSet(testDataPath, modelDataPath);
		}		
		
		Iterator<Question> iter = randomQuestions.iterator();
		
		displayPanel(iter, questions, trainingSet, model, parties);
	}
	
	private ArrayList<Question> getRandomQuestions(int numOfQuestions, ArrayList<Question> questions) {
		ArrayList<Question> bankOfQuestions = (ArrayList<Question>) questions.clone();
		ArrayList<Question> randomQuestions = new ArrayList<Question>();
		int actualNumberOfQuestions = numOfQuestions > questions.size() ? questions.size() : numOfQuestions;
		
		for (int i = 0; i < actualNumberOfQuestions; i++) {
			int randomIndex = getRandomNumber(0, bankOfQuestions.size());
			randomQuestions.add(bankOfQuestions.get(randomIndex));
			bankOfQuestions.remove(randomIndex);
		}
		
		return randomQuestions;
	}
	
	private void displayPanel(
		Iterator<Question> iter,
		ArrayList<Question> questions,
		TrainingSet trainingSet,
		TrainingModel model,
		String[] parties
	) {
		if (!iter.hasNext()) {
			System.out.println("Last Question!!!");
			if (trainingSet != null) {
				ArrayList<TrainingQuestion> trainingQuestions = trainingSet.getQuestions();
				String party = trainingQuestions.get(trainingQuestions.size() - 1).getAnswer();
				trainingSet.setParty(party);
				trainingSet.writeHistory();
				trainingSet.trainModel();
				return;
			} else {
				showResultPanel(parties[model.getProbablePartyIndex()]);
				return;
			}
		}
		
		Question question = iter.next();

		int width = 850;
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);	
		panel.setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));	
		
		
		ImageIcon icon = new ImageIcon(GUI.class.getResource("logo.png"));
		frame.setIconImage(icon.getImage());
		
		JLabel label = new JLabel("<html>" + question.getQuestion() + "</html>");
		panel.add(label, BorderLayout.CENTER);
		
		JButton nextBtn = new JButton("Next Question");
		nextBtn.setSize(200, 50);
		nextBtn.setEnabled(false);	
		
		ArrayList<String> currentChoices = new ArrayList<String>();
		
		ActionListener onCheckChoice = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton aBtn = (AbstractButton) e.getSource();
				currentChoices.add(aBtn.getActionCommand());
				nextBtn.setEnabled(true);
			}
		};
		
		
		ArrayList<JRadioButton> choiceBtns = renderChoices(question, onCheckChoice, panel);		
		
		panel.add(nextBtn);
		
		frame.add(panel);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Political Survey");
		frame.setSize(width, 400);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		ActionListener onNextQuestion = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton aBtn = (AbstractButton) e.getSource();
				if (currentChoices.size() != 0) {
					aBtn.setEnabled(false);
					String choice = currentChoices.get(currentChoices.size() - 1);
					
					clearFrame(frame, panel, aBtn, this, choiceBtns, onCheckChoice);
					
					if (model != null) {
						PartyAffiliation probableParty = model.addProbabilitiesToStatistic(
							model.getAffiliationsByQuestionAndChoice("" + question.getId(), choice)
						);
						
						if (probableParty != null && probableParty.getProbability() >= 1.0) {
							showResultPanel(parties[probableParty.getPartyIndex()]);
						}
					} else {
						TrainingQuestion tQuestion = new TrainingQuestion(question.getId() + "", choice);
						trainingSet.add(tQuestion);
					}
					
					
					displayPanel(iter, questions, trainingSet, model, parties);
				}
			}
		};
		nextBtn.addActionListener(onNextQuestion);
		
	}
	
	private void showResultPanel(String resulPartyName) {
		int width = 850;
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);	
		panel.setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));	
		
		
		ImageIcon icon = new ImageIcon(GUI.class.getResource("logo.png"));
		frame.setIconImage(icon.getImage());
		
		JLabel label = new JLabel("Your favourite party is " + resulPartyName);
		panel.add(label, BorderLayout.CENTER);
		
		frame.add(panel);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Results");
		frame.setSize(width, 400);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void clearFrame(
		JFrame frame,
		JPanel panel,
		AbstractButton aBtn,
		ActionListener onNext,
		ArrayList<JRadioButton> choiceBtns,
		ActionListener onCheckChoice
	) {
		frame.remove(panel);
		aBtn.removeActionListener(onNext);
		
		Iterator<JRadioButton> iterChoices = choiceBtns.iterator();
		while (iterChoices.hasNext()) {
			JRadioButton btn = iterChoices.next();
			btn.removeActionListener(onCheckChoice);
		}
		
		frame.removeAll();
		frame.setVisible(false);
	}
	
	private ArrayList<JRadioButton> renderChoices(Question question, ActionListener onCheckChoice, JPanel panel) {
		ButtonGroup btnGroup = new ButtonGroup();
		ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();

		Iterator<String> iter = question.getAnswerChoices().iterator();
		int index = 0;
		while(iter.hasNext()) {
			String choice = iter.next();
			
			JRadioButton choiceBtn = new JRadioButton(choice);
			choiceBtn.setActionCommand("" + index);
			
			choiceBtn.addActionListener(onCheckChoice);
			
			btnGroup.add(choiceBtn);			
			panel.add(choiceBtn);
			index++;
			buttons.add(choiceBtn);
		}
		
		return buttons;
	}
	
	private int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
}
