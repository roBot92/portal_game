package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Replicator extends Player{
	public static Random randomgenerator=new Random();
	public Replicator(Field startPos, int playerNumber, int weight) {
		super(startPos, playerNumber, weight);
	}

	@Override
	public boolean collectZPM(){
		return false;
	}
	
	@Override
	public boolean die(){
		isAlive=false;
		return true;
	}
	
	@Override
	public void step(int dir){
		Field nextField=pos.getNextField(dir);
		
		int counter=0; //hogy ne fusson örökké, ha mondjuk be van zárva 4 fal közé.
		while(nextField==pos&&counter<10){
			faceDir=randomgenerator.nextInt(4)+1;
			nextField=pos.getNextField(faceDir);
			
			counter++;
		}
		
		if(pos!=nextField){
			pos.stepOff(weight);
			nextField.stepOn(this, weight);
			pos=nextField;
		}		
	}
	
	@Override
	public void draw(Graphics g, int size){
		
		int x=pos.getPosX()*size*5+size*5/3;
		int y=pos.getPosY()*size*5+3;
		g.setColor(Color.CYAN);
		g.fillOval(x, y, size*2, size*5);
		
		x-=size*5/3;
		y+=size*5/3;
		g.fillOval(x, y, size*5, size*2);
		
	}
	
}
