package struct_ojas;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.junit.Before;
import org.junit.Test;

import struct_ojas.Bullet.bulletTypes;

public class TestWall {

	private Wall wall;
	private Door door;
	private Stargate stargate;
	private Field field;
	@Before
	public void setUp() throws Exception {
		door = mock(Door.class);
		stargate = mock(Stargate.class);
		field = mock(Field.class);
		Wall.stargate = stargate;

	}

	@Test
	public void getWormHoleExitOnSpecialWall() {
		
		wall = new Wall(field, true);
		
		when(stargate.getWormHoleExit(wall)).thenReturn(wall);
		
		assertTrue(wall.getWormHoleExit() == wall);
		verify(stargate, times(1)).getWormHoleExit(wall);
		
				
	}
	
	@Test
	public void getWormholeExitOnNonSpecialWall(){
		wall = new Wall(field, false);
		assertTrue(wall.getWormHoleExit() == wall);
		verify(stargate, never()).getWormHoleExit(wall);
	}
	
	@Test
	public void checkDoorWithoutDoor(){
		wall = new Wall(field, null);
		assertFalse(wall.checkDoor());
	}
	
	@Test
	public void checkDoorWithDoor(){
		wall = new Wall(field, door);
		when(door.isOpened()).thenReturn(false);
		
		assertFalse(wall.checkDoor());
		verify(door, times(1)).isOpened();
		
		when(door.isOpened()).thenReturn(true);
		
		assertTrue(wall.checkDoor());
		verify(door, times(2)).isOpened();
		
	}
	
	@Test
	public void setGateOnNonSpecialWall(){
		wall = new Wall(field, false);
		bulletTypes bulletType = bulletTypes.GREEN;
		
		wall.setGate(bulletType); 
		verify(stargate, never()).setGreenWall(wall);
		
	}
	
	@Test
	public void setGateOnSpecialWall(){
		wall = new Wall(field, true);
		bulletTypes bulletType = bulletTypes.GREEN;
		
		wall.setGate(bulletType); 
		verify(stargate, times(1)).setGreenWall(wall);
		
	}
	
	//On drawing tests we don't check types of lines, but the color and the number of invocations.
		@Test
		public void drawRegularWall(){
			wall = new Wall(field, false);
			Graphics2D g = mock(Graphics2D.class);
			
			wall.draw(g, 10, 1);
			verify(field, times(1)).getPosX();
			verify(field, times(1)).getPosY();
			verify(g, times(1)).setStroke(any(Stroke.class));
			//Only black
			verify(g, times(1)).setColor(Color.BLACK);
			
			verify(g, times(1)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
			
		}
		@Test
		public void drawDoor(){
			wall = new Wall(field, door);
			Graphics2D g = mock(Graphics2D.class);
			
			wall.draw(g, 10, 1);
			verify(field, times(1)).getPosX();
			verify(field, times(1)).getPosY();
			verify(door, times(1)).isOpened();
			verify(g, times(1)).setStroke(any(Stroke.class));
			
			//Only brown
			verify(g, times(1)).setColor(new Color(222, 184, 135));
			
			verify(g, times(1)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
			
		}
		
		@Test
		public void drawStargate(){
			wall = new Wall(field, true);
			Graphics2D g = mock(Graphics2D.class);
			
			stargate.blueWall = wall;
			wall.draw(g, 10, 1);
			verify(field, times(1)).getPosX();
			verify(field, times(1)).getPosY();
			verify(g, times(1)).setStroke(any(Stroke.class));
			
			
			verify(g, times(1)).setColor(Color.BLUE);
			
			verify(g, times(1)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
			
			stargate.blueWall = null;
			stargate.greenWall = wall;
			wall.draw(g, 10, 1);
			verify(g, times(1)).setColor(Color.GREEN);
			
			stargate.greenWall = null;
			stargate.redWall = wall;
			wall.draw(g, 10, 1);
			verify(g, times(1)).setColor(Color.RED);
			
			stargate.redWall = null;
			stargate.yellowWall = wall;
			wall.draw(g, 10, 1);
			verify(g, times(1)).setColor(Color.YELLOW);
		}
		
		
	

}
