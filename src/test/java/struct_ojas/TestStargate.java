package struct_ojas;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestStargate {


	@Test
	public void wormHoleWithNoExitTest() {
		Wall yellowWall = new Wall(new Field(), true);
		Stargate gate = new Stargate();
		gate.setYellowWall(yellowWall);
		assertEquals(yellowWall, gate.getWormHoleExit(yellowWall));
		
		Wall bluewWall = new Wall(new Field(), true);
		gate.setBlueWall(bluewWall);
		gate.setYellowWall(null);
		assertEquals(bluewWall, gate.getWormHoleExit(bluewWall));
		
		Wall redWall = new Wall(new Field(), true);
		gate.setRedWall(redWall);
		gate.setBlueWall(null);
		assertEquals(redWall, gate.getWormHoleExit(redWall));
		
		Wall greenWall = new Wall(new Field(), true);
		gate.setGreenWall(greenWall);
		gate.setRedWall(null);
		assertEquals(greenWall, gate.getWormHoleExit(greenWall));

	}
	
	@Test
	public void wormHoleWithExitTest() {
		Wall yellowWall = new Wall(new Field(), true);
		Wall bluewWall = new Wall(new Field(), true);
		Stargate gate = new Stargate();
		
		gate.setYellowWall(yellowWall);
		gate.setBlueWall(bluewWall);
		assertEquals(bluewWall, gate.getWormHoleExit(yellowWall));
		assertEquals(yellowWall, gate.getWormHoleExit(bluewWall));
		
		Wall redWall = new Wall(new Field(), true);
		Wall greenWall = new Wall(new Field(), true);
				
		gate.setRedWall(redWall);
		gate.setGreenWall(greenWall);
		assertEquals(greenWall, gate.getWormHoleExit(redWall));
		assertEquals(redWall, gate.getWormHoleExit(greenWall));

	}
	
}
