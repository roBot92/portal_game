package struct_ojas;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;
import java.awt.Graphics;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestField {

	private Field field;
	private Item item;
	private Field neighbour;
	private Wall wall;
	private Player player;
	private int dir = 1;
	private int oppositeDir = 3;

	@Before
	public void setUp() throws Exception {
		this.field = new Field();
		this.neighbour = mock(Field.class);
		this.item = mock(Item.class);
		this.wall = mock(Wall.class);
		this.player = mock(Player.class);
		
		this.field.setPos(10, 10);
		
		
	}

	

	@Test
	public void stepOffTest() {
		field.addNewItem(item);

		field.stepOff(0);
		verify(item, times(1)).onStepOffAction(0);

	}

	@Test
	public void addNewItemOnNonHoleTest() {
		// So item is not a black hole
		when(item.onDropAction(0)).thenReturn(false);
		Item item2 = mock(Item.class);
		field.addNewItem(item);
		field.addNewItem(item2);

		assertTrue(field.getItemCount() == 2);
		verify(item, times(1)).onDropAction(anyInt());
	}

	@Test
	public void addNewItemOnHoleTest() {
		// So item is a black hole
		when(item.onDropAction(0)).thenReturn(true);
		field.addNewItem(item);

		Item item2 = mock(Item.class);
		field.addNewItem(item2);

		assertTrue(field.getItemCount() == 1);
		verify(item, times(1)).onDropAction(anyInt());

	}

	@Test
	public void addNewNullItem() {
		field.addNewItem(null);
		assertTrue(field.getItemCount() == 0);
	}
	
	

	@Test
	public void stepToNeighbourWithClosedOwnWall() {

		field.setWall(dir, wall);
		field.setNeighbour(dir, neighbour);
		when(wall.getWormHoleExit()).thenReturn(wall);
		when(wall.checkDoor()).thenReturn(false);

		assertTrue("Closed wall without neighboiur.", field.getNextField(dir) == field);

		verify(wall, times(1)).getWormHoleExit();
		verify(wall, times(1)).checkDoor();

		field.setNeighbour(dir, neighbour);

		assertTrue("Closed wall with neighboiur.", field.getNextField(dir) == field);

		verify(wall, times(2)).getWormHoleExit();
		verify(wall, times(2)).checkDoor();

	}
	@Test
	public void stepToNeighbourWithClosedAndOpenedOtherWall(){
		
		when(neighbour.getWall(oppositeDir)).thenReturn(wall);
		when(wall.getWormHoleExit()).thenReturn(wall);
		when(wall.checkDoor()).thenReturn(false);

		field.setNeighbour(dir, neighbour);
		assertTrue("Closed wall on neighbour.", field.getNextField(dir) == field);

		verify(wall, times(1)).getWormHoleExit();
		verify(wall, times(1)).checkDoor();
		verify(neighbour,times(1)).getWall(oppositeDir);

		when(wall.checkDoor()).thenReturn(true);
		assertTrue("Opened wall on neighbour.", field.getNextField(dir) == neighbour);

		verify(wall, times(2)).getWormHoleExit();
		verify(wall, times(2)).checkDoor();
		verify(neighbour,times(2)).getWall(oppositeDir);
		
	}

	@Test
	public void stepToNeighbourWithOpenedOwnWall() {
		field.setWall(dir, wall);
		when(wall.getWormHoleExit()).thenReturn(wall);
		when(wall.checkDoor()).thenReturn(true);

		assertTrue("Opened wall without neighbour.", field.getNextField(dir) == field);

		verify(wall, times(1)).getWormHoleExit();
		verify(wall, times(1)).checkDoor();

		field.setNeighbour(dir, neighbour);

		assertTrue("Opened wall with neighboiur.", field.getNextField(dir) == neighbour);

		verify(wall, times(2)).getWormHoleExit();
		verify(wall, times(2)).checkDoor();
		
		
		verify(neighbour, times(1)).getWall(oppositeDir);
	}
	
	@Test
	public void stepAtTheVoidWithoutWall(){
		assertTrue(field.getNextField(dir) == field);
		
	}
	
	@Test
	public void stepToOtherFieldThroughStargate(){
		field.setWall(dir, wall);
		Wall otherWall = mock(Wall.class);
		
		when(wall.getWormHoleExit()).thenReturn(otherWall);
		when(otherWall.getOwnField()).thenReturn(neighbour);
		
		assertTrue(field.getNextField(dir) == neighbour);
		
		verify(wall, times(1)).getWormHoleExit();
		verify(wall, never()).checkDoor();
		verify(otherWall, times(1)).getOwnField();
		
		
	}
	
	@Test
	public void performPickWithoutItem(){
		
		assertNull(field.performPick());
	}
	
	@Test
	public void performPickWithNonPickableAndPickableItem(){
		when(item.onPickAction()).thenReturn(null);
		field.addNewItem(item);
		assertTrue("Cannot pick", field.performPick() == null && field.getItemCount() == 1);
		verify(item, times(1)).onPickAction();
		
		when(item.onPickAction()).thenReturn(item);
		assertTrue("Can pick", field.performPick() == item && field.getItemCount() == 0);
		verify(item, times(2)).onPickAction();
		verify(item, times(1)).getWeight();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void stepOnWithRemovableItem(){
		
		when(item.onStepAction(player, 0)).thenReturn(true);
		List<Item> itemsMock = mock(ArrayList.class);
	
		List<Item> items = Arrays.asList(item);
		when(itemsMock.iterator()).thenReturn(items.iterator());
		field.setItems(itemsMock);
		field.stepOn(player, 0);
		
		verify(item, times(1)).onStepAction(player, 0);
		verify(itemsMock, times(1)).remove(item);
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void stepOnWithUnremovableItem(){
		when(item.onStepAction(player, 0)).thenReturn(false);
		List<Item> itemsMock = mock(ArrayList.class);
	
		List<Item> items = Arrays.asList(item);
		when(itemsMock.iterator()).thenReturn(items.iterator());
		field.setItems(itemsMock);
		field.stepOn(player, 0);
		
		verify(item, times(1)).onStepAction(player, 0);
		verify(itemsMock, never()).remove(any());
	}
	
	@Test
	public void testDraw(){
		Graphics g = mock(Graphics.class);
		field.addNewItem(item);
		field.setWall(dir, wall);
		
		field.draw(g, 0);
		
		verify(item, times(1)).draw(g, 0, field.getPosX(), field.getPosY());
		verify(wall, times(1)).draw(g, 0, dir);
	}

}
