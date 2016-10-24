package VoxSpell.festival;

import javax.swing.SwingWorker;

import VoxSpell.gui.Quiz;

public class Speaker extends SwingWorker<Void, Void> {

	private Quiz _quiz;

	public Speaker(Quiz quiz) {
		_quiz = quiz;
	}

	@Override
	protected Void doInBackground() throws Exception {

		// Calls a bash command to run festival with the scheme file
		Bash.bashCommand("festival -b .text.scm");

		return null;
	}

	protected void done() {

		Bash.bashCommand("rm -f .text.scm");

		// If there is currently a quiz registered to the speaker object, then toggle
		// the submit, repeat and main menu buttons
		if (_quiz != null) {
			if (_quiz.getRepeats() > 0 ) {
				_quiz.toggleRepeat(true);
			}

			if (_quiz.getWord().getAttempts() == 2 || _quiz.getWord().isCorrect()) {
				_quiz.toggleSubmit(false);
				_quiz.toggleRepeat(false);
			} else {
				_quiz.toggleSubmit(true);
			}
			_quiz.enableMenu();
		}
	}

}
