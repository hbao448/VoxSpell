package VoxSpell.festival;

import java.io.IOException;

public class Bash {

	/**
	 * This method executes a single bash command through the use of a Process
	 * @param command The command to be executed using bash
	 */
	public static void bashCommand(String command) {

		ProcessBuilder a = new ProcessBuilder("bash", "-c", command);
		a.redirectErrorStream(true);

		try {
			Process b = a.start();
			b.waitFor();
		} catch (IOException | InterruptedException e) {
		}
	}
	
}
