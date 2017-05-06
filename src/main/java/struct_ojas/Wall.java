package struct_ojas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Wall {

	private boolean spec;
	protected static Stargate stargate=new Stargate();
	private Door door;
	private Field ownField;

	/**
	 *Speciális vagy sima fal létrehozása
	 *@param isSpecial Megadhatjuk, hogy lehessen-e rajta csillagkaput létrehozni
	 *@param myField A falat tartalmazó mezõ
	 */
	public Wall(Field myField, boolean isSpecial){
		spec=isSpecial;
		ownField=myField;
		door=null;
	}
	/*/**
	 *Speciális fal létrehozása(így nem tartalmazhat ajtót), és Stargate átadása
	 *@param stargate Az osztályváltozónak beállítandó objektum
	 *@param myField A falat tartalmazó mezõ
	 
	public Wall(Field myField, Stargate stargate){
		System.out.println("Konstruktor: Wall - speciális, stargate-tel");
		spec=true;
		ownField=myField;
		Wall.stargate=stargate;
		door=null;
	}*/
	/**
	 *Ajtót tartalmazó fal létrehozása(így nem tartalmazhat Stargatet)
	 *@param door A falon levõ ajtó
	 *@param myField A falat tartalmazó mezõ
	 */
	public Wall(Field myField, Door door){
		spec=false;
		ownField=myField;
		this.door=door;
	}
	/**
	 * Visszaadja féreglyuk másik oldalának mezõjét
	 * @param thisWall
	 */
	public Wall getWormHoleExit() {
		return stargate.getWormHoleExit(this);
	}
	/**
	 * 
	 * @return Az ajtó állapota
	 */
	public boolean checkDoor() {
		if(door==null) return false;
		return door.isOpened();
	}

	/**
	 * Létrehozza a paraméternek megfelelõ színû csillagkaput 
	 * @param isYellow Igaz, ha sárga csillagkaput kell létrehozni; Hamis, ha kéket
	 */
	public void setGate(Bullet.bulletTypes bulletType) {
		if(spec){
			switch(bulletType){
			case YELLOW: stargate.setYellowWall(this); break;
			case BLUE: stargate.setBlueWall(this); break;
			case GREEN: stargate.setGreenWall(this); break;
			case RED: stargate.setRedWall(this); break;
			default: break;
			}
		}
		
	}

	public Field getOwnField() {
		return ownField;
	}
	
	/*
	 * A fal kirajzoló függvénye. Függ az esetleges ajtótól, a fal pozíciójától és irányától, speciális mivoltától, illetve csillagkapu mivoltától.
	 * @param g - Graphics objektum, a gamepanel-hez tartozik.
	 * @param size - int objektum, a kirajzolás mérete függ tõle.
	 * @param orientation - a fal iránya
	 */
	public void draw(Graphics g, int size, int orientation) {
		
		int x1=0;
		int y1=0;
		int x2=0;
		int y2=0;
		
		//Kezdõ -és végpont meghatározása
		//Bal felsõ sarok a kezdõpont
		if(orientation==1||orientation==4){
			x1=ownField.getPosX()*size*5+5;
			y1=ownField.getPosY()*size*5;
			if(orientation==1){
				y2=y1;
				x2=x1+size*5;
			}
			else {
				x2=x1;
				y2=y1+size*5;
			}
		}
		
		//Jobb alsó sarok a kezdõpont
		else if(orientation==2||orientation==3){
			x1=(ownField.getPosX()+1)*size*5+5;
			y1=(ownField.getPosY()+1)*size*5;
			
			if(orientation==2){
				x2=x1;
				y2=y1-size*5;
			}
			else{
				y2=y1;
				x2=x1-size*5;
			}
		}
		
		float dash1[] = {10.0f};
		
		//Megvastagítjuk a vonalat.
		BasicStroke stroke=new BasicStroke(5);
		
		//ha speciális, vagy nyitott ajtó van rajta, akkor szaggatott.
		if(this.spec||(door!=null&&door.isOpened())){
			
					stroke=new BasicStroke(5,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);
		}
		Graphics2D g2=(Graphics2D)g;
		g2.setStroke(stroke);
		
		//Beállítjuk a színt
		//Ha ajtó, akkor barna
		if(door!=null){
			g2.setColor(new Color(222,184,135));
		}
		//ha speciális, és van rajta nyitott csillagkapu, akkor a megfelelõ csillagkapuszínû
		else if(this.spec){
			if(this==stargate.blueWall) g2.setColor(Color.BLUE);
			else if(this==stargate.redWall) g2.setColor(Color.RED);
			else if(this==stargate.greenWall) g2.setColor(Color.GREEN);
			else if(this==stargate.yellowWall) g2.setColor(Color.YELLOW);
			else g2.setColor(Color.BLACK);
		}
		//Egyébként fekete.
		else{
			g2.setColor(Color.BLACK);
		}
		
		
		g2.drawLine(x1, y1, x2, y2);
		
		
		
	}

}