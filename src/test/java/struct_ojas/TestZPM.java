package struct_ojas;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestZPM {


	@Test
	public void onStepActionTest() {
		Player player = new Player(new Field(), 1, 1);
		ZPM zpm = new ZPM();
		
		zpm.onStepAction(player, 0);
		assertEquals(1, player.getZPMCount());
	}
	
	
}
