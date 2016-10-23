package VoxSpell.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public abstract class AbstractBackgroundScreen extends AbstractScreen{
	
	private Image bg;
	
	public AbstractBackgroundScreen() {
		super();
		this.bg = new ImageIcon("resources/Images/Background.jpg").getImage();
		
	}
	
	 @Override
	 protected void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	       	g.drawImage(bg, 0, 0, 800, 600, null);
	    } 
}
