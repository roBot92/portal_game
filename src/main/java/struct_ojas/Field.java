package struct_ojas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mez� oszt�ly.
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
	 * Param�ter n�lk�li konstruktor. L�trehozza 4 elem� HashMap-k�nt a
	 * neighbours �s a walls Map-eket, �s felt�lti �ket null �rt�kekkel, illetve
	 * ArrayList-k�nt l�trehozza az items adattagot.
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
	 * A poz�ci� be�ll�t�s�ra szolg�l� setter f�ggv�ny.
	 * 
	 * @param x
	 *            - x poz�ci�
	 * @param y
	 *            - y poz�ci�
	 * 
	 */
	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	/**
	 * Az X poz�ci� lek�rdez�s�re szolg�l� f�ggv�ny
	 * 
	 * @return PosX
	 */
	public Integer getPosX() {
		return posX;
	}

	/**
	 * Az Y poz�ci� lek�rdez�s�re szolg�l� f�ggv�ny
	 * 
	 * @return PosY
	 */
	public Integer getPosY() {
		return posY;
	}

	/**
	 * Egy fal be�ll�t�s�ra val� f�ggv�ny.
	 * 
	 * @param i
	 *            - a Field-en bel�li helye (�szak - kelet - d�l - nyugat)
	 * @param w
	 *            - az �j falra mutat� referencia.
	 */
	public void setWall(int i, Wall w) {
		walls.put(i, w);
	}

	/**
	 * A k�vetkez� Field lek�r�se adott ir�nyban. L�trehozunk egy nextfield
	 * v�ltoz�t az adott ir�nyba lev� k�vetkez� mez�nek, egy myWall v�ltoz�t a
	 * mi adott ir�nyban lev� falunknak, illetve egy neighbourswall v�ltoz�t a
	 * szomsz�d megfelel� (azaz ellent�tes ir�nyban lev�) fal�nak.
	 * 
	 * Ezut�n 1. Ha nincs falunk, �s szomsz�dunk sem, visszaadjuk �nmagunkat.
	 * 2.Ha van falunk, megn�zz�k, hogy ny�lik -e r�la f�reglyuk, ha igen,
	 * visszaadjuk a kij�rat fal�nak mez�j�t. Ha nem, megn�zz�k, hogy van -e
	 * ajtaja nyitva. Ha nincs, z�rt ajt� van, vagy sima fal, vagyis visszaadjuk
	 * �nmagunkat. 3.Nyitott ajt� volt, ez�rt ugyanezt megism�telj�k a szomsz�d
	 * esetleges fal�n, ha van neki. 4. Ha m�g nem t�rt�nk vissza, akkor nyitott
	 * ajt� volt a szomsz�d fal�n is, megn�zz�k, hogy van -e szomsz�d mez�. Ha
	 * van, visszaadjuk azt, ha nincs, visszaadjuk �nmagunkat.
	 * 
	 * @param dir
	 *            - A k�vetkez� Field elhelyezked�se hozz�nk k�pest.
	 * @return Field t�pus� v�ltoz�.
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
	 * Adott szomsz�d be�ll�t�s�ra szolg�l� f�ggv�ny.
	 * 
	 * @param i
	 *            - A szomsz�d ir�nya.
	 * @param f
	 *            - Maga a szomsz�d Field.
	 */
	public void setNeighbour(int i, Field f) {
		neighbours.put(i, f);

	}

	/**
	 * Az adott ir�nyban l�v� fal lek�rdez�s�re szolg�l� f�ggv�ny. Ha nem tal�l
	 * falat a saj�t mez�h�z, m�g megn�zi a szomsz�dos mez�t is.
	 * 
	 * @param dir
	 *            - ir�ny
	 * @return Wall
	 */
	public Wall getWall(int dir) {
		return walls.get(dir);
	}

	public Field getNeighbour(int dir) {
		return neighbours.get(dir);
	}

	/**
	 * Adott mez�r�l val� t�rgy felv�teli k�s�rletekor megh�v�d� f�ggv�ny. az
	 * items utols� elem�re megh�vjuk az onPickAction-t(ha van rajta box,
	 * biztosan az utols� helyen lesz) azt az elemet az itemsb�l, �s megh�vja
	 * saj�t stepOff(int) f�ggv�ny�t. A v�g�n visszat�r az ideiglenes Item-mel,
	 * ami sikeress�g eset�n egy Item (�s Box), egy�bk�nt null.
	 * 
	 * @return Item sikeres felv�tel eset�n, egy�bk�nt null. Szerkesztve:
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
	 * Player Field-re l�p�sekor megh�v�d� f�ggv�ny. L�trehoz egy ideiglenes
	 * Item-et (temp), majd v�gigiter�lva az items List-en mindenkire megh�vja
	 * az onStepAction()-t. Ha true-val t�rt vissza, akkor a temp-nek �rt�k�l
	 * adjuk az adot Item-et. A v�g�n, ha temp m�r nem null, vagyis ZPM volt,
	 * kit�r�lj�k a az items-b�l az adott Item-et, ami biztosan ZPM volt.
	 * 
	 * @param player
	 *            - A l�p� Player.
	 * @param weight
	 *            - A r�l�p� j�t�kos s�lya.
	 */
	public void stepOn(Player player, int weight) {
		Item temp = null;
		for (Item i : items) {
			boolean isRemovableItem = i.onStepAction(player, weight);
			if (isRemovableItem)
				temp = i;// Ha minden j�l m�k�dik, csak egy elt�vol�that�
							// objektum lehet rajta, egy zpm, vagy egy lyuk.
		}

		if (temp != null)
			items.remove(temp);
	}

	/**
	 * Adott Fieldr�l val� lel�p�s, illetve doboz leszed�se eset�n megh�v�d�
	 * f�ggv�ny. V�gigiter�l az items List-en, �s mindenkire megh�vja az
	 * onStepOffAction(int) f�ggv�nyt. A lel�p� j�t�kos, vagy levett t�rgy
	 * s�ly�val.
	 */
	public void stepOff(int weight) {
		// System.out.println("Field.stepOff()");

		for (Item i : items) {
			i.onStepOffAction(weight);
		}

	}

	/**
	 * A Field-hez �j Item-et tev� f�ggv�ny. L�trehoz egy ideiglenes boolean
	 * v�ltoz�t, false-k�nt. V�gigiter�l az items List-en, megh�vja mindegyiken
	 * az onDropAction-t, �s ha valahol true-val t�rt vissza (azaz egy Hole van
	 * a mez�n), akkor az item-et megsemmis�tj�k. Egy�bk�nt hozz�adjuk az
	 * items-hez.
	 * 
	 * @param item
	 *            - A hozz�adand� item.
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
	 * A kirajzol�s�rt felel�s f�ggv�ny.
	 * 
	 * @param g - Graphics objektum, amire rajzolunk
	 * 
	 * @param size - Integer objektum, a m�ret�rt felel�s
	 */
	public void draw(Graphics g, int size) {

		// A falak kirajzol�sa
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