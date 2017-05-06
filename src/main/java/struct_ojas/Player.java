package struct_ojas;

import java.awt.Color;
import java.awt.Graphics;



public class Player {

	protected Field pos;
	protected int faceDir;
	private int ZPMCount;
	private Item box;
	protected boolean isAlive;
	protected int weight;
	private Bullet.bulletTypes bulletType;
	public boolean hasBox;
	
	/**
	 * 
	 * @param startPos A játékos kezdõ pozíciója
	 * @param playerNumber - A játékos típusa == a beállatható színû lövedékeket
	 * tudjuk meg.
	 * @param weight - a játékos súlya
	 */
	public Player(Field startPos, int playerNumber, int weight){		
		if(playerNumber==1)bulletType=Bullet.bulletTypes.YELLOW;
		else bulletType=Bullet.bulletTypes.RED;
		
		hasBox = false;
		isAlive = true;
		box = null;
		pos=startPos;
		faceDir=1;
		this.weight=weight;
	}
	/**
	 * A játékos lépése a kapott irányba
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 * @param dir A lépés iránya
	 */
	public void step(int dir) {
		Field newPos;
		faceDir=dir;
		newPos= pos.getNextField(dir);
		if (newPos!=pos) {
			pos.stepOff(weight);
			newPos.stepOn(this, weight);
			pos=newPos;
		}
	}
	/**
	 * A játékos nézésének fordítása +-90 fokkal
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 * @param dir A forgatás iránya
	 */
	public void turn() {
		faceDir+=2;
		if(faceDir>4) faceDir-=4;
	}
	/**
	 * Ellenõrizzük, hogy a nézésnek megfelelõ irányban van-e doboz, és ha igen felvesszük
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public void pickUp() {
		Field tempfield=pos.getNextField(faceDir);
		if (pos!=tempfield&&!hasBox) //nem egy fal elõtt állunk, van elõttünk elérhetõ másik mezõ
		{	//és nincs a kezünkben box
			box=tempfield.performPick();
			
			if(box!=null) {
				hasBox=true;
				this.weight+=box.getWeight();
			}
			else hasBox=false;
		}
		
	}

	/**
	 * Ellenõrizzük, hogy a nézésnek megfelelõ irányban van-e mezõ, és ha igen letesszük a dobozt
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public void putDown() {
		Field putToThisField=pos.getNextField(faceDir);
		if (pos!=putToThisField&&hasBox) //nem egy fal elõtt állunk, van elõttünk elérhetõ másik mezõ
		{	//és van a kezünkben box
			putToThisField.addNewItem(box);
			hasBox=false;
			weight-=box.getWeight();
			box=null;
		}
	}

	/**
	 * Növeljük a ZPM számlálót, a Field hívja meg a stepOn(player:Player függvényében)
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public boolean collectZPM() {	
		ZPMCount++;
		return true;
	}

	/**
	 * A játékos meghal
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public boolean die() {
		isAlive=false;
		return false;
	}

	/**
	 * A játékos lead egy lövést
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public void fire() {
		Bullet bullet=new Bullet(pos, bulletType);
		bullet.firing(faceDir);
	}

	/**
	 * A játékos megváltoztatja a lövedék típusát
	 * @author Pacz Benjamin
	 * @date 2016.03.28
	 */
	public void changeBulletType() {
		switch(this.bulletType){
		case BLUE: this.bulletType=Bullet.bulletTypes.YELLOW; break;
		case YELLOW: this.bulletType=Bullet.bulletTypes.BLUE; break;
		case GREEN: this.bulletType=Bullet.bulletTypes.RED; break;
		case RED: this.bulletType=Bullet.bulletTypes.GREEN; break;
		default: break;
		}
	}
	
	public String getBulletType() {
		return this.bulletType.toString();
	}
	
	/**
	 * Visszaadja az életének állapotát, azaz, hogy él -e még.
	 * @author Boti
	 * @date 2016.04.23.
	 */
	public boolean isAlive(){
		return isAlive;
	}

	/**
	 * Visszaadja a begyûjtött ZPM-ek számát.
	 * @author Boti
	 * @date 2016.04.23.
	 */
	public int getZPMCount(){
		return this.ZPMCount;
	}
	
	/*
	 * A játékos kirajzolásáért felelõ függvény
	 * Ismeri a saját pozícióját, irányát, lövedéke típusát, és hogy van -e nála doboz.
	 * @param g - Graphics objektum, a játékpanelé, erre rajzolunk.
	 * @param size - int, a méretet ez alapján tudjuk.
	 */
	public void draw(Graphics g, int size) {
		if(isAlive){
			// Játékos arca
			
			
			int r=(size-1)*5;
			int realPosX=pos.getPosX()*size*5+6;
			int realPosY=pos.getPosY()*size*5+3;
			
			//A lövedéktípus alapján eldöntjük, hogy melyik játékos vagyunk.
			if(bulletType==Bullet.bulletTypes.BLUE||bulletType==Bullet.bulletTypes.YELLOW)
			g.setColor(new Color(245,194,151));
			
			else{
				g.setColor(new Color(128,0,128));
			}
			g.fillOval(realPosX, realPosY, r, r);
			
			//Játékos fegyvere
			switch(this.bulletType){
			case RED: g.setColor(Color.RED); break;
			case GREEN: g.setColor(Color.GREEN); break;
			case YELLOW: g.setColor(Color.YELLOW); break;
			case BLUE: g.setColor(Color.BLUE); break;
			}
			
			int x=0;
			int y=0;
			int width=0;
			int height=0;
			
			//vízszintes állású fegyver
			if(faceDir==2||faceDir==4){
				width=size*5/2;
				height=size*5/6;
				y=pos.getPosY()*size*5+size*5/6*3;
				if(faceDir==2){
					x=pos.getPosX()*size*5+size*5/6*5;
				}
				else if(faceDir==4){
					x=pos.getPosX()*size*5-size;
				}
				
			}
			
			else{
				width=size*5/6;
				height=size*5/2;
				x=pos.getPosX()*size*5+size*5/6*3;
				if(faceDir==1){
					y=pos.getPosY()*size*5-size;
				}
				else if(faceDir==3){
					y=pos.getPosY()*size*5+size*5*4/6;
				}
			}
			
			g.fillRect(x, y, width, height);
			
			//Ha van nálunk box, elegánsan a fejünkön hordjuk.
			if(box!=null){
				x=pos.getPosX()*size*5+size*5/4+5;
				y=pos.getPosY()*size*5+size*5/4+5;
				width=size*5/2;
				height=size*5/2;
				
				g.setColor(new Color(222,184,135));
				g.fillRect(x, y, width, height);
			}
		}
		
		
	}
}