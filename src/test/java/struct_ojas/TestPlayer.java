package struct_ojas;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TestPlayer {

	@Test
	public void stepTest() {
		Field pos = mock(Field.class);
		Field nextField = mock(Field.class);
		Player player = new Player(pos, 1, 100);
		
		when(pos.getNextField(1)).thenReturn(nextField);
		
		player.step(1);
		
		verify(pos, times(1)).stepOff(anyInt());
		verify(nextField, times(1)).stepOn(player, 100);
		assertEquals(nextField, player.pos);
		
	}
	
	@Test
	public void turnTest() {
		Field pos = mock(Field.class);
		Player player = new Player(pos, 1, 100);
		
		player.turn();
		
		assertEquals(player.faceDir, 3);		
	}
	
	@Test
	public void pickUpTest() {
		Field pos = mock(Field.class);
		Field nextField = mock(Field.class);
		Box box = new Box(10);
		Player player = new Player(pos, 1, 100);
		
		when(pos.getNextField(1)).thenReturn(nextField);
		
		player.pickUp();//nem vesz fel semmit
		assertFalse(player.hasBox);
		assertEquals(player.weight, 100);
		
		when(nextField.performPick()).thenReturn(box);
		
		player.pickUp();//felveszi a dobozt
		assertTrue(player.hasBox);
		assertEquals(player.weight, 110);
		
		when(pos.getNextField(1)).thenReturn(pos);
		player.pickUp();//nem vesz fel dobozt mégegyszer
		assertTrue(player.hasBox);
		assertEquals(player.weight, 110);
	}
	
	@Test
	public void putDownTest() {
		Field pos = mock(Field.class);
		Field nextField = mock(Field.class);
		Box box = new Box(10);
		Player player = new Player(pos, 1, 100);
		
		when(pos.getNextField(1)).thenReturn(nextField);
		when(nextField.performPick()).thenReturn(box);
		
		player.pickUp();
		player.putDown();
		assertFalse(player.hasBox);
		assertEquals(player.weight, 100);
		
		when(pos.getNextField(1)).thenReturn(pos);
		player.putDown();//nem teszi le mégegyszer
		assertFalse(player.hasBox);
		assertEquals(player.weight, 100);
	}
	
	@Test
	public void collectZPMTest() {
		Field pos = mock(Field.class);
		Player player = new Player(pos, 1, 100);
		
		player.collectZPM();
		
		assertEquals(player.getZPMCount(), 1);
	}
	
	@Test
	public void dieTest() {
		Field pos = mock(Field.class);
		Player player = new Player(pos, 1, 100);
		
		player.die();
		
		assertFalse(player.isAlive());
	}
	
	@Test
	public void changeBulletTypeTest() {
		Field pos = mock(Field.class);
		Player player = new Player(pos, 1, 100);
		
		player.changeBulletType();
		assertEquals( player.getBulletType(), "BLUE");
		player.changeBulletType();
		assertEquals( player.getBulletType(), "YELLOW");
		
		
		Player player2 = new Player(pos, 5, 100);
		
		player2.changeBulletType();
		assertEquals( player2.getBulletType(), "GREEN");
		player2.changeBulletType();
		assertEquals( player2.getBulletType(), "RED");
	}
	
	
}
