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

	// If the quiz is complete, then disable the submit button, otherwise
	// re-enable the submit button
	protected void done() {

		Bash.bashCommand("rm -f .text.scm");

		if (_quiz != null) {
			if (_quiz.getRepeats() > 0 ) {
				_quiz.toggleRepeat(true);
			}

			if (_quiz.getAttempts() == 2 || _quiz.isCorrect()) {
				_quiz.toggleSubmit(false);
				_quiz.toggleRepeat(false);
			} else {
				_quiz.toggleSubmit(true);
			}
		}
	}

}
