package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Direction;

public class DirectionTest {
	@Test
	public void testDirection() {
		Direction direction = Direction.North;
		assertEquals(Direction.North, direction);
		
		direction = Direction.South;
		assertEquals(Direction.South, direction);
		
		direction = Direction.West;
		assertEquals(Direction.West, direction);
		
		direction = Direction.East;
		assertEquals(Direction.East, direction);
		
		direction = Direction.None;
		assertEquals(Direction.None, direction);
	}
}
