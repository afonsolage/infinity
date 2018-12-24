package com.lagecompany.infinity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.lagecompany.infinity.environment.terrain.IsometricTileMap;
import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class IsometricTileMapTest extends InfinityTest {

	private static IsometricTileMap map;

	@BeforeAll
	static void setup() {
		map = new IsometricTileMap(new TerrainBuffer(true));
	}

	@AfterAll
	static void teardown() {
		map.dispose();
	}

	@Test
	void testInstanciateIsometricTileMap() {
		Assertions.assertThrows(RuntimeException.class, () -> new IsometricTileMap(null));
		Assertions.assertThrows(RuntimeException.class, () -> new IsometricTileMap(new TerrainBuffer()));
		Assertions.assertThrows(RuntimeException.class, () -> new IsometricTileMap(new TerrainBuffer(false)));
		Assertions.assertNotNull(new IsometricTileMap(new TerrainBuffer(true)));
	}

	@Test
	void testCellCount() {
		TerrainBuffer buffer = new TerrainBuffer(true);
		IsometricTileMap isometricTileMap = new IsometricTileMap(buffer);
		Assertions.assertEquals(0, isometricTileMap.countCells());

		CellRef cell = buffer.getCell(10);
		cell.setTileType(1);
		cell.save();
		isometricTileMap.act(0);
		
		Assertions.assertEquals(1, isometricTileMap.countCells());
		
		cell.setTileType(0);
		cell.save();
		isometricTileMap.act(0);
		
		Assertions.assertEquals(0, isometricTileMap.countCells());
	}
}
