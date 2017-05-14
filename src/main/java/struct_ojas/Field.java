package struct_ojas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mezõ osztály.
 * 
 * @author Boti
 */
public class Field {

	private Integer posX;
	private Integer posY;
	private Map<Integer, Field> neighbours;
	protected List<Item> items;
	private Map<Integer, Wall> walls;

	/**
	 * Paraméter nélküli konstruktor. Létrehozza 4 elemû HashMap-ként a
	 * neighbours és a walls Map-eket, és feltölti õket null értékekkel, illetve
	 * ArrayList-ként létrehozza az items adattagot.
	 */
	public Field() {
		neighbours = new HashMap<Integer, Field>();
		walls = new HashMap<Integer, Wall>();
		for (int i = 1; i <= 4; i++) {
			neighbours.put(i, null);
			walls.put(i, null);
		}
		items = new ArrayList<Item>();
	}

	/**
	 * A pozíció beállítására szolgáló setter függvény.
	 * 
	 * @param x
	 *            - x pozíció
	 * @param y
	 *            - y pozíció
	 * 
	 */
	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	/**
	 * Az X pozíció lekérdezésére szolgáló függvény
	 * 
	 * @return PosX
	 */
	public Integer getPosX() {
		return posX;
	}

	/**
	 * Az Y pozíció lekérdezésére szolgáló függvény
	 * 
	 * @return PosY
	 */
	public Integer getPosY() {
		return posY;
	}

	/**
	 * Egy fal beállítására való függvény.
	 * 
	 * @param i
	 *            - a Field-en belüli helye (észak - kelet - dél - nyugat)
	 * @param w
	 *            - az új falra mutató referencia.
	 */
	public void setWall(int i, Wall w) {
		walls.put(i, w);
	}

	/**
	 * A következõ Field lekérése adott irányban. Létrehozunk egy nextfield
	 * változót az adott irányba levõ következõ mezõnek, egy myWall változót a
	 * mi adott irányban levõ falunknak, illetve egy neighbourswall változót a
	 * szomszéd megfelelõ (azaz ellentétes irányban levõ) falának.
	 * 
	 * Ezután 1. Ha nincs falunk, és szomszédunk sem, visszaadjuk önmagunkat.
	 * 2.Ha van falunk, megnézzük, hogy nyílik -e róla féreglyuk, ha igen,
	 * visszaadjuk a kijárat falának mezõjét. Ha nem, megnézzük, hogy van -e
	 * ajtaja nyitva. Ha nincs, zárt ajtó van, vagy sima fal, vagyis visszaadjuk
	 * önmagunkat. 3.Nyitott ajtó volt, ezért ugyanezt megismételjük a szomszéd
	 * esetleges falán, ha van neki. 4. Ha még nem tértünk vissza, akkor nyitott
	 * ajtó volt a szomszéd falán is, megnézzük, hogy van -e szomszéd mezõ. Ha
	 * van, visszaadjuk azt, ha nincs, visszaadjuk önmagunkat.
	 * 
	 * @param dir
	 *            - A következõ Field elhelyezkedése hozzánk képest.
	 * @return Field típusú változó.
	 */

	public Field getNextField(int dir) {
		Field nextField = neighbours.get(dir);
		Wall myWall = walls.get(dir);
		Wall neighboursWall = null;

		int oppositeDir = dir + 2;
		if (oppositeDir > 4)
			oppositeDir -= 4;
		if (nextField != null)
			neighboursWall = nextField.getWall(oppositeDir);

		if (myWall == null && nextField == null)
			return this;

		if (myWall != null) {
			Wall wormholeExitOfOwnWall = myWall.getWormHoleExit();
			if (wormholeExitOfOwnWall != myWall) {

				return wormholeExitOfOwnWall.getOwnField();
			}

			else if (myWall.checkDoor() == false) {
				return this;
			}
		}

		if (neighboursWall != null) {
			Wall wormholeExitOfONeighboursWall = neighboursWall.getWormHoleExit();
			if (wormholeExitOfONeighboursWall != neighboursWall) {
				return wormholeExitOfONeighboursWall.getOwnField();
			}

			else if (neighboursWall.checkDoor() == false)
				return this;
		}

		if (nextField != null)
			return nextField;
		else
			return this;

	}

	/**
	 * Adott szomszéd beállítására szolgáló függvény.
	 * 
	 * @param i
	 *            - A szomszéd iránya.
	 * @param f
	 *            - Maga a szomszéd Field.
	 */
	public void setNeighbour(int i, Field f) {
		neighbours.put(i, f);

	}

	/**
	 * Az adott irányban lévõ fal lekérdezésére szolgáló függvény. Ha nem talál
	 * falat a saját mezõhöz, még megnézi a szomszédos mezõt is.
	 * 
	 * @param dir
	 *            - irány
	 * @return Wall
	 */
	public Wall getWall(int dir) {
		return walls.get(dir);
	}

	public Field getNeighbour(int dir) {
		return neighbours.get(dir);
	}

	/**
	 * Adott mezõrõl való tárgy felvételi kísérletekor meghívódó függvény. az
	 * items utolsó elemére meghívjuk az onPickAction-t(ha van rajta box,
	 * biztosan az utolsó helyen lesz) azt az elemet az itemsbõl, és meghívja
	 * saját stepOff(int) függvényét. A végén visszatér az ideiglenes Item-mel,
	 * ami sikeresség esetén egy Item (és Box), egyébként null.
	 * 
	 * @return Item sikeres felvétel esetén, egyébként null. Szerkesztve:
	 *         2016.04.23
	 */
	public Item performPick() {
		if (items.size() != 0) {
			Item temp = items.get(items.size() - 1).onPickAction();
			if (temp != null) {
				items.remove(temp);
				this.stepOff(temp.getWeight());
			} else {
			}
			return temp;
		}
		return null;
	}

	/**
	 * Player Field-re lépésekor meghívódó függvény. Létrehoz egy ideiglenes
	 * Item-et (temp), majd végigiterálva az items List-en mindenkire meghívja
	 * az onStepAction()-t. Ha true-val tért vissza, akkor a temp-nek értékül
	 * adjuk az adot Item-et. A végén, ha temp már nem null, vagyis ZPM volt,
	 * kitöröljük a az items-bõl az adott Item-et, ami biztosan ZPM volt.
	 * 
	 * @param player
	 *            - A lépõ Player.
	 * @param weight
	 *            - A rálépõ játékos súlya.
	 */
	public void stepOn(Player player, int weight) {
		Item temp = null;
		for (Item i : items) {
			boolean isRemovableItem = i.onStepAction(player, weight);
			if (isRemovableItem)
				temp = i;// Ha minden jól mûködik, csak egy eltávolítható
							// objektum lehet rajta, egy zpm, vagy egy lyuk.
		}

		if (temp != null)
			items.remove(temp);
	}

	/**
	 * Adott Fieldrõl való lelépés, illetve doboz leszedése esetén meghívódó
	 * függvény. Végigiterál az items List-en, és mindenkire meghívja az
	 * onStepOffAction(int) függvényt. A lelépõ játékos, vagy levett tárgy
	 * súlyával.
	 */
	public void stepOff(int weight) {
		// System.out.println("Field.stepOff()");

		for (Item i : items) {
			i.onStepOffAction(weight);
		}

	}

	/**
	 * A Field-hez új Item-et tevõ függvény. Létrehoz egy ideiglenes boolean
	 * változót, false-ként. Végigiterál az items List-en, meghívja mindegyiken
	 * az onDropAction-t, és ha valahol true-val tért vissza (azaz egy Hole van
	 * a mezõn), akkor az item-et megsemmisítjük. Egyébként hozzáadjuk az
	 * items-hez.
	 * 
	 * @param item
	 *            - A hozzáadandó item.
	 */
	public void addNewItem(Item item) {
		boolean isHole = false;
		if (item != null) {
			for (Item i : items) {
				isHole = i.onDropAction(item.getWeight());
				if (isHole) {
					item = null;
					break;
				}
			}
			if (item != null) {
				items.add(item);
			}

		}

	}

	public int getItemCount() {
		return items.size();
	}

	public void setItems(List<Item> items){
		this.items = items;
	}
	/*
	 * A kirajzolásért felelõs függvény.
	 * 
	 * @param g - Graphics objektum, amire rajzolunk
	 * 
	 * @param size - Integer objektum, a méretért felelõs
	 */
	public void draw(Graphics g, int size) {

		// A falak kirajzolása
		Wall drawable = null;
		for (int i = 1; i <= 4; i++) {
			drawable = walls.get(i);
			if (drawable != null) {
				drawable.draw(g, size, i);
			}
		}

		for (Item i : items) {
			i.draw(g, size, posX, posY);
		}

	}
}