package struct_ojas;

import java.awt.Graphics;

/**
 * @author Boti
 * Absztrakt item osztály.
 */
public abstract class Item {

	/**
	 * A rálépés megvalósítása.
	 * @param player A leszármazottak használják.
	 * @return false - True esetén a tulajdonos Field kitörölné az adott Item-et.
	 */
	public boolean onStepAction(Player player, int weight) {
		return false;
	}

	/**
	 * Doboz felvétele, csak a Box override-olja.
	 * @return null- Box esetén önmaga.
	 */
	public Item onPickAction() {
		return null;
	}

	/**
	 * A mezõrõl való lelépéskor, vagy doboz levételekor hívódik meg, csak a Scale-re van hatása.
	 * 
	 */
	public void onStepOffAction(int weight) {}
	
	/**
	 * Doboz rádobásakor hívódik meg. A Scale és a Hole override-olja, elõbbi ajtót nyit, utóbbi true-val tér vissza.
	 * @return boolean - Hole esetén true, hogy a Field tudja, hogy meg kell semmisíteni a rádobott Box-ot, egyéb esetben false.
	 * 
	 */
	public boolean onDropAction(int weight) {
		return false;
	}
	
	public int getWeight(){
		return 0;
	}

	public void draw(Graphics g, int size, Integer posX, Integer posY) {
		// TODO Auto-generated method stub
		
	}

}