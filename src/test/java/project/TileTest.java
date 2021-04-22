package project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


class TileTest {
	private Tile tile;
	
	@BeforeEach
	void setup() {
		tile = new Tile(0, 0, false);
	}

	@Test
	void testConstructor1() {
		tile = new Tile(2, 3, true);
		assertTrue(tile.isBomb());
		assertEquals(tile.getY(), 2);
		assertEquals(tile.getX(), 3);
		assertThrows(IllegalArgumentException.class, 
					() -> tile = new Tile(-1, 3, true));
		assertThrows(IllegalArgumentException.class, 
					() -> tile = new Tile(3, -2, true));
	}
	
	@Test
	void testConstructor2() {
		tile = new Tile(4, 5, false, 4);
		assertFalse(tile.isBomb());
		assertEquals(tile.getY(), 4);
		assertEquals(tile.getX(), 5);
		assertEquals(tile.getValue(), 4);
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(2, 3, true, -1));
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(-2, 3, true,1));
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(2, -3, true, 1));
	}
	
	@Test
	void testConstructor3() {
		tile = new Tile(5, 6, false, true, false, 8);
		assertEquals(tile.getY(), 5);
		assertEquals(tile.getX(), 6);
		assertEquals(tile.getValue(), 8);
		assertFalse(tile.isBomb());
		assertTrue(tile.isFlagged());
		assertFalse(tile.isVisible());
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(2, 3, true, false, false, -1));
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(-2, 3, true, false, false, 1));
		assertThrows(IllegalArgumentException.class, 
				() -> tile = new Tile(2, -3, true, false, false, 1));
	}
	
	@Test
	void testSetValue() {
		tile.setValue(5);
		assertEquals(tile.getValue(), 5);
		assertThrows(IllegalArgumentException.class,
				() -> tile.setValue(-5));
	}
	
	@Test
	void testToStringForFileHandling() {
		tile = new Tile(3, 4, false, true, false, 6);
		List<String> list = new ArrayList<>(Arrays.asList(tile.toStringForFileHandling().split(":")));
		assertFalse(Boolean.parseBoolean(list.get(0)));
		assertTrue(Boolean.parseBoolean(list.get(1)));
		assertFalse(Boolean.parseBoolean(list.get(2)));
		assertEquals(Integer.parseInt(list.get(3)), 6);
	}
	

}
