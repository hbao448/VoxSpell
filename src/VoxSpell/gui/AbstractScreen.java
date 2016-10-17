package VoxSpell.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractScreen extends JPanel{
	
	public AbstractScreen() {
		super();
	}
	
	 @Override
     public Dimension getPreferredSize() {
         return new Dimension(800, 600);
     }	
}
