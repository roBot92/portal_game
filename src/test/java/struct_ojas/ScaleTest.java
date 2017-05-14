package struct_ojas;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ScaleTest {

	private Scale scale;
	private Door door;

	@Before
	public void setUp() throws Exception {
		door = mock(Door.class);
		scale = new Scale(door, 0);
	}

	@Test
	public void stepOnTest() {
		Player player = mock(Player.class);
		assertFalse(scale.onStepAction(player, Scale.weightThreshold / 2));
		assertTrue(scale.load == Scale.weightThreshold / 2);
		verify(door, never()).open();
		verify(player, never()).die();

		assertFalse(scale.onStepAction(player, Scale.weightThreshold / 2));
		assertTrue(scale.load == Scale.weightThreshold);
		verify(door, times(1)).open();
		verify(player, never()).die();

	}

	@Test
	public void onStepOffAction() {
		scale = new Scale(door, Scale.weightThreshold*2);
		scale.onStepOffAction(Scale.weightThreshold);
		assertTrue(scale.load == Scale.weightThreshold);
		verify(door, never()).close();
		
		scale.onStepOffAction(Scale.weightThreshold);
		assertTrue(scale.load == 0);
		verify(door, times(1)).close();
	}
	
	@Test
	public void dropOnTest() {
	
		assertFalse(scale.onDropAction(Scale.weightThreshold / 2));
		assertTrue(scale.load == Scale.weightThreshold / 2);
		verify(door, never()).open();

		assertFalse(scale.onDropAction(Scale.weightThreshold / 2));
		assertTrue(scale.load == Scale.weightThreshold);
		verify(door, times(1)).open();

	}

}
