package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;

public class ZPM extends Item {

	/**
	 * Meghívja a kapott játékoson a collectZPM függvényt, így a játékosnál (ha nem replikátor)
	 * növekedni fog a ZPM-ek száma, illetve a Field ki fogja törölni
	 * a ZPM-et az Item-ek közül.
	 * @param player
	 * @param weight - A rákerülõ súly.
	 */
	@Override
	public boolean onStepAction(Player player, int weight) {
		
		return player.collectZPM();
		
	}	
	
	@Override
	public void draw(Graphics g, int size, Integer posX, Integer posY){
		int x=posX*size*5+size*5/4+5;
		int y=posY*size*5+size*5/4+5;
		int width=size*5/2;
		int height=size*5/2;
		
		g.setColor(Color.PINK);
		g.fillRect(x, y, width, height);
	}

}