package struct_ojas;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TestBullet {


	@Test
	public void firingTest() {
		Field field = mock(Field.class);
		Field fieldWithWall = mock(Field.class);
		Wall wall = new Wall(fieldWithWall, true);
		Bullet bullet = new Bullet(field, Bullet.bulletTypes.BLUE);
				
		when(field.getNextField(1)).thenReturn(fieldWithWall);
		when(fieldWithWall.getNextField(1)).thenReturn(fieldWithWall);
		when(fieldWithWall.getWall(1)).thenReturn(wall);
		
		bullet.firing(1);
		assertEquals(wall, Wall.stargate.blueWall);
		
	}
	
	
}
