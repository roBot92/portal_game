package struct_ojas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestHole{


	@Test
	public void onStepActionTest() {
		Player player = new Player(new Field(), 1, 1);
		Hole hole = new Hole();
		
		hole.onStepAction(player, 0);
		assertEquals(false, player.isAlive());
		
	}
	
	@Test
	public void onDropActionTest() {
		Hole hole = new Hole();
		
		assertEquals(true, hole.onDropAction(0));
	}
	
}
