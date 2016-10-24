package VoxSpell.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * This class is used as the main content pane of the application, and any
 * screen to be shown in the application must extend it.
 * @author Hunter
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractScreen extends JPanel{

	@Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }	
	
}
