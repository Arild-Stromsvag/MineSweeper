package project;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GameTest {

	private Game[] games;
	private Game game1;
	private Game game2;
	private Game game3;
	private Game game4;
	
	@BeforeEach
	public void setup() {
		games = new Game[4];
		game1 = new Game(1);
		game2 = new Game(2);
		game3 = new Game(3);
		game4 = new Game(4);
		games[0] = game1;
		games[1] = game2;
		games[2] = game3;
		games[3] = game4;
	}
	
	@Test
	public void testConstructor() {
		assertEquals(10, game1.getHeight());
		assertEquals(10, game1.getMines());
		
		assertEquals(12, game2.getHeight());
		assertEquals(20, game2.getMines());
		
		assertEquals(14, game3.getHeight());
		assertEquals(35, game3.getMines());
		
		assertEquals(16, game4.getHeight());
		assertEquals(55, game4.getMines());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game1 = new Game(0));
		assertThrows(IllegalArgumentException.class, 
				() -> game1 = new Game(5));
	}
	
	@Test
	public void testSetFirstPressedTile() {
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setFirstPressedTile(-1, 0));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setFirstPressedTile(9, -1));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setFirstPressedTile(9, 10));
		game1.setFirstPressedTile(9, 9);
		assertTrue(game1.getTile(9, 9).isVisible());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game2.setFirstPressedTile(11, 12));
		game2.setFirstPressedTile(11, 11);
		assertTrue(game2.getTile(11, 11).isVisible());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game3.setFirstPressedTile(13, 14));
		game3.setFirstPressedTile(13, 13);
		assertTrue(game3.getTile(13, 13).isVisible());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game4.setFirstPressedTile(15, 16));
		game4.setFirstPressedTile(15, 15);
		assertTrue(game4.getTile(15, 15).isVisible());
		
	}
	
	@Test
	public void testSetNeighborTilesToZero() {
		for (Game game : games) {
			game.setFirstPressedTile(4, 6);
			game.setNeighborTilesToZero(4, 6);
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					assertEquals(game.getTile(4+i, 6+j).getValue(), 0);
				}
			}
		}
	}
	
	@Test
	public void testLayOutMines() {
		for (Game game : games) {
			game.setFirstPressedTile(5, 5);
			game.setNeighborTilesToZero(5, 5);
			game.layOutMines();
			assertEquals(game.getMines(), game.getListOfMines().size());
			for (Tile bomb : game.getListOfMines()) {
				assertEquals(true, bomb.isBomb());
			}
		}
	}

	@Test
	public void testSetTiles() {
		for (Game game : games) {
			game.setFirstPressedTile(6, 5);
			game.setNeighborTilesToZero(6, 5);
			game.layOutMines();
			game.setTiles();
			List<Tile> listOfMines = new ArrayList<>();
			List<Tile> listOfNormalTiles = new ArrayList<>();
			for (int y = 0; y < game.getHeight(); y++) {
				for (int x = 0; x < game.getWidth(); x++) {
					if (game.getBoard()[y][x].isBomb())
						listOfMines.add(game.getBoard()[y][x]);
					else
						listOfNormalTiles.add(game.getBoard()[y][x]);
				}
			}
			assertEquals(game.getMines(), listOfMines.size());
			assertEquals(game.getHeight()*game.getWidth() - game.getMines(), listOfNormalTiles.size());
			
		}
	}
	
//	Baserer testen av calculateValue() på dette brettet her som er toStringen til testBoard1. "x" er bomber og de andre verdien til tilesene.
//	Kom dessverre ikke på noen bedre måte å teste denne funksjonen uten at det blir for komplekst.
//	x 2 1 2 1 1 0 0 0 0 
//	1 2 x 2 x 2 1 1 0 0 
//	1 2 2 2 1 2 x 2 2 2 
//	1 x 1 0 0 1 1 2 x x 
//	1 1 1 0 0 0 0 1 2 2 
//	1 1 1 0 0 0 0 0 0 0 
//	1 x 1 1 1 1 0 0 0 0 
//	1 1 1 1 x 1 0 0 0 0 
//	1 1 1 1 1 1 0 0 0 0 
//	1 x 1 0 0 0 0 0 0 0 
	
	@Test
	public void testCalculateValue() {
		game1.setListOfMinesAndLayOut(game1.getTestBoard1());
		game1.setTiles();
		game1.setValueOfTiles();
		assertEquals(2, game1.calculateValue(1, 1));
		assertEquals(2, game1.calculateValue(2, 2));
		assertEquals(0, game1.calculateValue(3, 3));
		assertEquals(0, game1.calculateValue(4, 4));
		assertEquals(1, game1.calculateValue(5, 2));
		assertEquals(1, game1.calculateValue(0, 0));
		assertEquals(0, game1.calculateValue(9, 9));
		assertEquals(0, game1.calculateValue(0, 9));
		assertEquals(1, game1.calculateValue(9, 0));
	}
	
	@Test
	public void testSetValueOfTiles() {
		for (Game game : games) {
			game.setFirstPressedTile(6, 5);
			game.setNeighborTilesToZero(6, 5);
			game.layOutMines();
			game.setTiles();
			game.setValueOfTiles();
			for (int y = 0; y < game.getHeight(); y++) {
				for (int x = 0; x < game.getWidth(); x++) {
					assertEquals(game.calculateValue(y, x), game.getTile(y, x).getValue());
				}
			}
		}
	}
	
	/*
	Baserer også testen av makeNeighborTilesVisible() på testBoard1 da dette er en rekursiv metode og jeg ikke
	finner noen god måte å teste det på enn å ta utgangspunkt i et ferdig generert brett.
	x 2 1 2 1 1 0 0 0 0 
	1 2 x 2 x 2 1 1 0 0 
	1 2 2 2 1 2 x 2 2 2 
	1 x 1 0 0 1 1 2 x x 
	1 1 1 0 0 0 0 1 2 2 
	1 1 1 0 0 0 0 0 0 0 
	1 x 1 1 1 1 0 0 0 0 
	1 1 1 1 x 1 0 0 0 0 
    1 1 1 1 1 1 0 0 0 0 
	1 x 1 0 0 0 0 0 0 0
	*/
	
	@Test
	public void testMakeNeighborTilesVisible() {
		game1.setListOfMinesAndLayOut(game1.getTestBoard1());
		game1.setTiles();
		game1.setValueOfTiles();
		
		//setter i gang den rekursive metoden i de "blokkene" med nullere
		game1.makeNeighborTilesVisible(0, 9); 
		game1.makeNeighborTilesVisible(9, 9);
		
		//tester et utvalg av tiles, og sjekker om de er blitt visible slik de skal.
		assertTrue(game1.getTile(9, 3).isVisible());
		assertTrue(game1.getTile(8, 2).isVisible());
		assertTrue(game1.getTile(8, 5).isVisible());
		assertTrue(game1.getTile(7, 9).isVisible());
		assertTrue(game1.getTile(5, 2).isVisible());
		assertTrue(game1.getTile(3, 2).isVisible());
		assertTrue(game1.getTile(2, 9).isVisible());
		assertTrue(game1.getTile(1, 5).isVisible());
		
		//tester et utvalg av tiles, og sjekker om de ikke er visible.
		assertFalse(game1.getTile(7, 4).isVisible());
		assertFalse(game1.getTile(9, 0).isVisible());
		assertFalse(game1.getTile(3, 8).isVisible());
		assertFalse(game1.getTile(0, 0).isVisible());
	}
	
	@Test
	public void testIsValidTile() {
		for (Game game : games) {
			// tester alle hjørnene
			assertTrue(game.isValidTile(game.getHeight() - 1, game.getWidth() -1));
			assertTrue(game.isValidTile(0, 0));
			assertTrue(game.isValidTile(0, game.getWidth() - 1));
			assertTrue(game.isValidTile(game.getHeight() - 1, 0));
			
			//tester at de ytre kantene ikke er gyldige
			assertFalse(game.isValidTile(-1, -1));
			assertFalse(game.isValidTile(-1, game.getWidth()));
			assertFalse(game.isValidTile(game.getHeight(), -1));
			assertFalse(game.isValidTile(game.getHeight(), game.getWidth()));
		}
	}
	
	@Test
	public void testSetTile() {
		assertNull(game1.getTile(5, 5));
		game1.setTile(5, 5);
		assertTrue(game1.getTile(5, 5) instanceof Tile);
		assertEquals(5, game1.getTile(5, 5).getY());
		assertEquals(5, game1.getTile(5, 5).getX());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile(-1, 5));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile(5, -1));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile(10, 9));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile(9, 10));
		
	}
	
	@Test
	public void testSetTile2() {
		assertNull(game1.getTile(3, 4));
		game1.setTile2(3, 4, false, true, false, 2);
		assertEquals(3, game1.getTile(3, 4).getY());		
		assertEquals(4, game1.getTile(3, 4).getX());
		assertFalse(game1.getTile(3, 4).isBomb());
		assertTrue(game1.getTile(3, 4).isFlagged());
		assertFalse(game1.getTile(3, 4).isVisible());
		assertEquals(2, game1.getTile(3, 4).getValue());
		
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile2(9, 9, false, true, true, -1));
		assertThrows(IllegalArgumentException.class, 
				() -> game1.setTile2(9, 9, false, true, true, 10));

	}
}
