package struct_ojas;

public class Stargate {

	protected Wall yellowWall;
	protected Wall blueWall;
	protected Wall redWall;
	protected Wall greenWall;

	/**
	 * Megadja a féregjárat kijáratát/túlsó végét, vagy visszaadja ugyanazt a falat.
	 * @param otherWall - A féregjárat kapuja ahol belépünk
	 * Szerkesztve: 2016.04.23.
	 */
	public Wall getWormHoleExit(Wall otherWall) {
		if(yellowWall==otherWall&&blueWall!=null) return blueWall;		
		else if (blueWall==otherWall&&yellowWall!=null) return yellowWall;
		else if (redWall==otherWall&&greenWall!=null) return greenWall;
		else if (greenWall==otherWall&&redWall!=null) return redWall;
		else return otherWall;				
	}

	/**
	 * Beállít egy kék színû kaput a kapott falra
	 * @param wall - A fal amelyikre a kaput be kell állítani
	 */
	public void setBlueWall(Wall wall) {
		blueWall=wall;
	}

	/**
	 * Beállít egy piros színû kaput a kapott falra
	 * @param wall - A fal, amire a kaput be kell állítani
	 */
	public void setRedWall(Wall wall) {
		redWall=wall;
	}
	
	/**
	 * Beállít egy sárga színû kaput a kapott falra
	 * @param wall - A fal, amire a kaput be kell állítani
	 * 
	 */
	public void setYellowWall(Wall wall) {
		yellowWall=wall;
	}
	
	/**
	 * Beállít egy zöld színû kaput a kapott falra
	 * @param wall - A fal, amire a kaput be kell állítani
	 */
	public void setGreenWall(Wall wall) {
		greenWall=wall;
	}

}