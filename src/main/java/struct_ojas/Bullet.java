package struct_ojas;

public class Bullet {
	/*Szerkesztve: 2016.04.23*/
	public enum bulletTypes {BLUE, YELLOW,RED,GREEN};
	private bulletTypes type;
	private Field pos;

	/**
	 * Konstruktor
	 * @param dir
	 */
	public Bullet(Field startPos, bulletTypes bulletType) {		
		pos=startPos; //létrejön a lövedék a játékos mezején
		type=bulletType;
	}

	/**
	 * A töltény reptének vezénylése
	 * @param dir A játékos nézésének irány a lövés leadásakor
	 * Szerkesztve: 2016.04.23.
	 */
	public void firing(int dir) {		
		Field newfield=pos.getNextField(dir);//elkezd repülni
		
		while(pos!=newfield){  //repül ugyanabba az irányba amíg tud
			pos=newfield;
			newfield=pos.getNextField(dir);
		}
		
		
		Wall targetwall=pos.getWall(dir);//megismerkedik a fallal amibe ütközött
		
		if(targetwall==null||targetwall.checkDoor()==true){
			int oppositeDir=dir+2;
			if(oppositeDir>4)oppositeDir-=4;
			try{
				targetwall=newfield.getNeighbour(dir).getWall(oppositeDir);
			}
			catch(NullPointerException e){
				
			}
		}
		
		if(targetwall!=null)
		targetwall.setGate(type);
	}

}