package VoxSpell.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class AbstractScreen extends JPanel{

	@Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }	
	
}
