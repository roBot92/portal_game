package struct_ojas;

import java.awt.Graphics;
import java.util.Random;




public class Maze {

	private Field[][] fields;
	protected int sizeX, sizeY;
	
	
	/**
	 *Random ZPM hozz�ad�s�ra val� f�ggv�ny
	 *�res Field-et keres egy randomgener�tor seg�ts�g�vel.
	 *Ha tal�l, hozz�adja az �j ZPM-et.
	 *Ha 200 pr�b�lkoz�sb�l sem tal�l, le�ll eredm�nytelen�l.
	 */
	public void addRandomZPM(){
		int randomX=0, randomY=0;
		boolean foundEmptyField=false;
		
		int loopCounter=0; //ne fusson a v�gtelens�gig
		
		Random rand=new Random();
		
		while(foundEmptyField==false&&loopCounter<200){
			randomX=rand.nextInt(sizeX-1);
			randomY=rand.nextInt(sizeY-1);
			
			if(fields[randomX][randomY].getItemCount()==0){
				foundEmptyField=true;
			}
			loopCounter++;
		}
		
		if(loopCounter<200) fields[randomX][randomY].addNewItem(new ZPM());		
		
	}

	/**
	 *Konstruktor
	 *L�trehpzza a fields[][] 2D t�mbj�t, �s be�ll�tja a megfelel� szomsz�dokat.
	 *@param sizeX, sizeY - A labirintus m�retei
	 */
	public Maze(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		fields=new Field[sizeX][sizeY];
		
		for(int i=0;i<sizeX;i++){
			for(int j=0;j<sizeY;j++){
				fields[i][j]=new Field();
			}
		}
		
		for(int x=0;x<sizeX;x++){
			for(int y=0;y<sizeY;y++){
				fields[x][y].setPos(x,y);
				if(y!=sizeY-1)fields[x][y].setNeighbour(3, fields[x][y+1]);
				if(y!=0) fields[x][y].setNeighbour(1, fields[x][y-1]);
				
				if(x!=sizeX-1) fields[x][y].setNeighbour(2, fields[x+1][y]);
				if(x!=0) fields[x][y].setNeighbour(4, fields[x-1][y]);
				
			}
		}
		
	}
	
	
	public Player addPlayerToCorner(int playerNum, int cornerIndex){
		Field f;
		switch(cornerIndex){
		case 0: f=fields[0][0]; break;
		case 1: f=fields[sizeX-1][0]; break;
		case 2: f=fields[sizeX-1][sizeY-1]; break;
		case 3: f=fields[0][sizeY-1]; break;
		default: f=fields[0][0]; break;
		}
		
		return new Player(f,playerNum, 30);
		
		
	}
	public Field getField(int x,int y){
		return fields[x][y];
	}
	
	public void addZPM(int x, int y){
		fields[x][y].addNewItem(new ZPM());
	}
	
	public void addBox(int x, int y, int weight){
		fields[x][y].addNewItem(new Box(weight));
	}
	
	/**
	 *Ajt� n�lk�li fal beilleszt�s�re haszn�lt f�ggv�ny.
	 *@param x,y - A fal mez�j�nek a koordin�t�i.
	 *@param dir - A fal helye a mez�n bel�l.
	 *@param isSpecia - A fal speci�lis tulajdon�sga.
	 */
	public void addWall(int x, int y, int dir, boolean isSpecial){
		fields[x][y].setWall(dir,new Wall(fields[x][y],isSpecial));
	}
	
	public void addHole(int x, int y){
		fields[x][y].addNewItem(new Hole());
	}
	
	/**
	 *Ajt�s fal hozz�ad�s�ra szolg�l� f�ggv�ny.
	 *L�trehozunk egy ajt�t, �s egy m�rleget 0-�s teherrel, amihez hozz�k�tj�k az
	 *ajt�t.
	 *A megfelel� helyre berakjuk az �j falat, illetve a m�rleget.
	 *@param x,y - A fal mez�j�nek koordin�t�i.
	 *@param dir - A fal helye a field-en bel�l.
	 *@param scaleX,scaleY - A m�rleg helye.
	 */
	public void addWallWithDoor(int x, int y, int dir, int scaleX, int scaleY){
		Door door=new Door();
		Scale scale=new Scale(door, 0);
		
		fields[x][y].setWall(dir, new Wall(fields[x][y],door));
		
		fields[scaleX][scaleY].addNewItem(scale);
		
	}

	public void draw(Graphics g, int size) {
		
		int fieldsize=size*5;
		
		g.setColor(java.awt.Color.GRAY);
		for(int i=0;i<=sizeX;i++){
			g.drawLine(i*fieldsize+5,0,i*fieldsize+5,sizeY*fieldsize);
		}
		
		for(int i=0;i<=sizeY;i++){
			g.drawLine(5,i*fieldsize,sizeX*fieldsize+5,i*fieldsize);
		}
		
		for(int x=0 ; x<sizeX ; x++){
			for(int y=0 ; y<sizeY ; y++){
				fields[x][y].draw(g, size);
			}
		}
		
	}
	
	

	
}