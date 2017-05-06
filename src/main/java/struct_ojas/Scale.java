
package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;

public class Scale extends Item {

	private Door myDoor;
	int load=0;
	static int weightThreshold=10;

	/**
	 * A játékos rálépésekor lefutó függvény, ami kinyitja az ajtót.
	 * @param player - A játékos
	 *@return - false, lásd a Field osztálynál.
	 */
	
	@Override
	public boolean onStepAction(Player player, int weight) {
		//System.out.println("\tScale.onStepAction(Player)");
		load+=weight;
		if(load>=weightThreshold){
			myDoor.open();
		}
		
		//System.out.println("\t<-- Scale.onStepAction");		
		return false;

	}
	
	public Scale(Door myDoor, int load) {
		super();
		this.myDoor = myDoor;
		this.load = load;
	}
	/**
	 * A játékos lelépésekor lefutó függvény, ami becsukja az ajtót, ha
	 * a súlyküszöb alá ment a load.
	 * @param - a levett súly.
	 */
	@Override
	public void onStepOffAction(int weight) {
		this.load-=weight;
		
		if(load<weightThreshold){
			myDoor.close();
		}
		
	}
	
	/**
	 * Egy item ledobásakor lefutó függvény, hasonló az onStepAction()-hez.
	 * A load-hoz hozzáadjuk a kapott súlyt, és ha elérjük a súlyhatárt,
	 * kinyitjuk az ajtót.
	 * False-szal tér vissza - lásd: Field osztály.
	 * @param weight - int
	 * @return false 
	 * 
	 */
	@Override
	public  boolean onDropAction(int weight) {
		load+=weight;
		if(load>=weightThreshold){
			myDoor.open();
		}
		return false;
		
	}
	
	/**
	 *Az ajtó beállítására szolgáló függvény
	 * @param d - Door
	 * 
	 */
	public void setDoor(Door d){
		myDoor=d;
	}
	
	@Override
	public void draw(Graphics g, int size, Integer posX, Integer posY){
		int x=posX*size*5+size*5/4+5;
		int y=posY*size*5+size*5/2+5;
		int width=size*5/4*3;
		int height=size*5/3;
		
		//A mérleg alsó része
		g.setColor(Color.blue);
		g.fillRect(x, y, width, height);
		
		//A mérleg felsõ része
		int r=size/2*5;
		int realPosX=posX*size*5+size*5/2;
		int realPosY=posY*size*5+size*5*2/6;
		g.fillOval(realPosX, realPosY, r, r);
	}

}
