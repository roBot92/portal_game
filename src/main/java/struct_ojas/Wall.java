package struct_ojas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Wall {

	private boolean spec;
	protected static Stargate stargate = new Stargate();
	private Door door;
	private Field ownField;

	/**
	 * Speci�lis vagy sima fal l�trehoz�sa
	 * 
	 * @param isSpecial
	 *            Megadhatjuk, hogy lehessen-e rajta csillagkaput l�trehozni
	 * @param myField
	 *            A falat tartalmaz� mez�
	 */
	public Wall(Field myField, boolean isSpecial) {
		spec = isSpecial;
		ownField = myField;
		door = null;
	}

	/*
	 * /** Speci�lis fal l�trehoz�sa(�gy nem tartalmazhat ajt�t), �s Stargate
	 * �tad�sa
	 * 
	 * @param stargate Az oszt�lyv�ltoz�nak be�ll�tand� objektum
	 * 
	 * @param myField A falat tartalmaz� mez�
	 * 
	 * public Wall(Field myField, Stargate stargate){ System.out.println(
	 * "Konstruktor: Wall - speci�lis, stargate-tel"); spec=true;
	 * ownField=myField; Wall.stargate=stargate; door=null; }
	 */
	/**
	 * Ajt�t tartalmaz� fal l�trehoz�sa(�gy nem tartalmazhat Stargatet)
	 * 
	 * @param door
	 *            A falon lev� ajt�
	 * @param myField
	 *            A falat tartalmaz� mez�
	 */
	public Wall(Field myField, Door door) {
		spec = false;
		ownField = myField;
		this.door = door;
	}

	/**
	 * Visszaadja f�reglyuk m�sik oldal�nak mez�j�t
	 * 
	 * @param thisWall
	 */
	public Wall getWormHoleExit() {
		if (spec) {
			return stargate.getWormHoleExit(this);
		}
		return this;
	}

	/**
	 * 
	 * @return Az ajt� �llapota
	 */
	public boolean checkDoor() {
		if (door == null)
			return false;
		return door.isOpened();
	}


	/**
	 * L�trehozza a param�ternek megfelel� sz�n� csillagkaput
	 * 
	 * @param Bullet.bulletTypes enum
	 *            
	 */
	public void setGate(Bullet.bulletTypes bulletType) {
		if (spec) {
			switch (bulletType) {
			case YELLOW:
				stargate.setYellowWall(this);
				break;
			case BLUE:
				stargate.setBlueWall(this);
				break;
			case GREEN:
				stargate.setGreenWall(this);
				break;
			case RED:
				stargate.setRedWall(this);
				break;
			default:
				break;
			}
		}

	}

	public Field getOwnField() {
		return ownField;
	}

	/*
	 * A fal kirajzol� f�ggv�nye. F�gg az esetleges ajt�t�l, a fal poz�ci�j�t�l
	 * �s ir�ny�t�l, speci�lis mivolt�t�l, illetve csillagkapu mivolt�t�l.
	 * 
	 * @param g - Graphics objektum, a gamepanel-hez tartozik.
	 * 
	 * @param size - int objektum, a kirajzol�s m�rete f�gg t�le.
	 * 
	 * @param orientation - a fal ir�nya
	 */
	public void draw(Graphics g, int size, int orientation) {

		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;

		// Kezd� -�s v�gpont meghat�roz�sa
		// Bal fels� sarok a kezd�pont
		if (orientation == 1 || orientation == 4) {
			x1 = ownField.getPosX() * size * 5 + 5;
			y1 = ownField.getPosY() * size * 5;
			if (orientation == 1) {
				y2 = y1;
				x2 = x1 + size * 5;
			} else {
				x2 = x1;
				y2 = y1 + size * 5;
			}
		}

		// Jobb als� sarok a kezd�pont
		else if (orientation == 2 || orientation == 3) {
			x1 = (ownField.getPosX() + 1) * size * 5 + 5;
			y1 = (ownField.getPosY() + 1) * size * 5;

			if (orientation == 2) {
				x2 = x1;
				y2 = y1 - size * 5;
			} else {
				y2 = y1;
				x2 = x1 - size * 5;
			}
		}

		float dash1[] = { 10.0f };

		// Megvastag�tjuk a vonalat.
		BasicStroke stroke = new BasicStroke(5);

		// ha speci�lis, vagy nyitott ajt� van rajta, akkor szaggatott.
		if (this.spec || (door != null && door.isOpened())) {

			stroke = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(stroke);

		// Be�ll�tjuk a sz�nt
		// Ha ajt�, akkor barna
		if (door != null) {
			g2.setColor(new Color(222, 184, 135));
		}
		// ha speci�lis, �s van rajta nyitott csillagkapu, akkor a megfelel�
		// csillagkapusz�n�
		else if (this.spec) {
			if (this == stargate.blueWall)
				g2.setColor(Color.BLUE);
			else if (this == stargate.redWall)
				g2.setColor(Color.RED);
			else if (this == stargate.greenWall)
				g2.setColor(Color.GREEN);
			else if (this == stargate.yellowWall)
				g2.setColor(Color.YELLOW);
			else
				g2.setColor(Color.BLACK);
		}
		// Egy�bk�nt fekete.
		else {
			g2.setColor(Color.BLACK);
		}

		g2.drawLine(x1, y1, x2, y2);

	}

}