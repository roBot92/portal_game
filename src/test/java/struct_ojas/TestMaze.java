package struct_ojas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMaze {


	@Test
	public void addRandomZPMTest() {
		Maze maze = new Maze(10,10);
		
		maze.addRandomZPM();
		
		int itemCount = 0;
		
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if (maze.getField(i, j).getItemCount()!= 0 ) itemCount++;
			}
		}
		
		assertTrue(itemCount == 1);
	}
	
	@Test
	public void addPlayerToCornerTest() {
		Maze maze = new Maze(10,10);
		Player player = maze.addPlayerToCorner(1, 0);
		
		assertEquals(player.pos, maze.getField(0, 0));
		
		player = maze.addPlayerToCorner(1, 1);
		assertEquals(player.pos, maze.getField(9, 0));
		
		player = maze.addPlayerToCorner(1, 2);
		assertEquals(player.pos, maze.getField(9, 9));
		
		player = maze.addPlayerToCorner(1, 3);
		assertEquals(player.pos, maze.getField(0, 9));
		
		player = maze.addPlayerToCorner(1, 100);//default position
		assertEquals(player.pos, maze.getField(0, 0));
		
	}
	
	@Test
	public void addZPMTest() {
		Maze maze = new Maze(10,10);
		maze.addZPM(2, 1);
		
		assertEquals(maze.getField(2, 1).getItemCount(), 1);
		assertTrue(maze.getField(2, 1).items.get(0) instanceof ZPM);
	}
	
	@Test
	public void addBoxTest() {
		Maze maze = new Maze(10,10);
		maze.addBox(2, 1, 100);
		
		assertEquals(maze.getField(2, 1).getItemCount(), 1);
		assertTrue(maze.getField(2, 1).items.get(0) instanceof Box);
		assertTrue(maze.getField(2, 1).items.get(0).getWeight() == 100);
	}
	
	@Test
	public void addWallTest() {
		Maze maze = new Maze(10,10);
		maze.addWall(2, 1, 1, true);
		
		assertTrue(maze.getField(2, 1).getWall(1) != null);
	}
	
	@Test
	public void addHoleTest() {
		Maze maze = new Maze(10,10);
		maze.addHole(2, 1);
		
		assertEquals(maze.getField(2, 1).getItemCount(), 1);
		assertTrue(maze.getField(2, 1).items.get(0) instanceof Hole);
	}
	
	@Test
	public void addWallWithDoorTest() {
		Maze maze = new Maze(10,10);
		maze.addWallWithDoor(2, 1, 1, 3,4);
		
		assertTrue(maze.getField(2, 1).getWall(1) != null);
		assertTrue(maze.getField(3, 4).items.get(0) instanceof Scale);
	}
	
}
