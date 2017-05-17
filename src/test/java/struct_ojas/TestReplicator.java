package struct_ojas;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class TestReplicator {


	@Test
	public void collectZPMTest() {
		assertFalse(new Replicator(new Field(), 1, 100).collectZPM());
	}
	
	@Test
	public void dieTest() {
		Replicator replicator = new Replicator(new Field(), 1, 100);
		replicator.die();
		assertFalse(replicator.isAlive());
	}
	
	@Test
	public void stepTest() {
		Field nextField = mock(Field.class);
		Field mockField = mock(Field.class);
		Replicator replicator = new Replicator(mockField, 1, 100);
		
		when(mockField.getNextField(1)).thenReturn(nextField);
		
		replicator.step(1);
		
		verify(nextField, times(1)).stepOn(replicator, 100);
		assertEquals(replicator.pos, nextField);
	}
	
	@Test
	public void canNotStepTest() {
		Field mockField = mock(Field.class);
		Replicator replicator = new Replicator(mockField, 1, 100);
		
		when(mockField.getNextField(anyInt())).thenReturn(mockField);
		
		replicator.step(1);
		
		verify(mockField, times(11)).getNextField(anyInt());
	}
	
}
