package struct_ojas;

import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel{
	private Drawable controller;
	int size;
	
	public GamePanel(Drawable c, int size){
		super();
		this.controller=c;
		this.size=size;
		
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		controller.draw(g, size);
		
		
	}
}
