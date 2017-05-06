package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;

public class Hole extends Item {

	/**
	 * A játékoson meghívott die() függvény értékével tér vissza, így
	 * így megöli a játékost, és ha replikátor volt, akkor a tartalmazó
	 * Field ki fogja törölni ezt a Hole-t.
	 * @param player
	 * @return player.die() - Innen tudjuk, hogy replikátor volt -e.
	 */
	@Override
	public boolean onStepAction(Player player, int weight) {		
		return player.die();
	}
	
	@Override
	public boolean onDropAction(int weight) {
		return true;
	}
	
	@Override
	public void draw(Graphics g, int size, Integer posX, Integer posY){
		int r=(size-1)*5;
		int realPosX=posX*size*5+6;
		int realPosY=posY*size*5+3;
		g.setColor(Color.BLACK);
		g.fillOval(realPosX, realPosY, r, r);
	}

}