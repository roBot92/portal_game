package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;

public class Box extends Item {
	/*
	 * Konstruktor
	 * @author Pacz Benjámin
	 * @date 2016.03.28.
	 */
	int weight;
	
	public Box(int weight){
		this.weight=weight;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see Item#onPickAction()
	 */
	@Override
	public Box onPickAction() {
		return this;
	}
	
	@Override
	public int getWeight(){
		return weight;
	}
	
	@Override
	public void draw(Graphics g, int size, Integer posX, Integer posY){
		int x=posX*size*5+size*5/4+5;
		int y=posY*size*5+size*5/4+5;
		int width=size*5/2;
		int height=size*5/2;
		
		g.setColor(new Color(222,184,135));
		g.fillRect(x, y, width, height);
	}

}