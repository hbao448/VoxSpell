package VoxSpell.media;

import javax.swing.SwingWorker;
import VoxSpell.festival.Bash;

public class VideoMaker extends SwingWorker<Void,Void>{

	private int level;
	private String name;
	
	public VideoMaker(int level, String name) {
		this.level = level;
		this.name = name;
	}
	
	@Override
	protected Void doInBackground() {
		
		String command = "";
		command += "ffmpeg -i \"resources/Videos/Video Reward.avi\" -y -vf \"[in]drawtext=fontfile='resources/Fonts/njnaruto.ttf': text='Congratulations ";
		command += name;
		command += "': fontcolor=white: fontsize=24: x=(w-text_w)/2: y=10, drawtext=fontfile='resources/Fonts/njnaruto.ttf': text='You passed level ";
		command += level+"";
		command += " good work': fontcolor=black: fontsize=24: x=(w-text_w)/2: y=(h-30)\" -codec:a copy \"resources/GeneratedVideos/";
		command += "Reward" + level + ".avi\"";
		
		Bash.bashCommand(command);
		return null;
	}

}
