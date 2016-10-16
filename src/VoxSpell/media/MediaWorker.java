package VoxSpell.media;

import javax.swing.SwingWorker;

import VoxSpell.festival.Bash;

public class MediaWorker extends SwingWorker<Void,Void> {
	
	private String _command;
	
	public MediaWorker(String command) {
		_command = command;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		Bash.bashCommand(_command);
		return null;
	}
}
