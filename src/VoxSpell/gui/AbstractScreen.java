package VoxSpell.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractScreen extends JPanel{
	
	private Image bg;
	
	public AbstractScreen() {
		super();
		this.bg = new ImageIcon("resources/Background.jpg").getImage();
		
	}
	
	 @Override
     public Dimension getPreferredSize() {
         return new Dimension(800, 600);
     }	
	 
	 @Override
	 protected void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	       	g.drawImage(bg, 0, 0, 800, 600, null);
	    } 
}
