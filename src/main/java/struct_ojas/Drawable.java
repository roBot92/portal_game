package struct_ojas;

import java.awt.Graphics;

/*
 * A kirajzolható interfész. Egy darab függvénye van.
 */
public interface Drawable {
	/*
	 * A kirajzolás függvénye. A paraméterben kapott Graphics objektumra rajzol.
	 * @param - Graphics objektum
	 */
	public void draw(Graphics g, int size);
}
