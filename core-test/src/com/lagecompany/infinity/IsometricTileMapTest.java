package com.lagecompany.infinity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.lagecompany.infinity.environment.terrain.IsometricTileMap;
import com.lagecompany.infinity.environment.terrain.TerrainController;
import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class IsometricTileMapTest extends InfinityTest {

	private static IsometricTileMap map;
	private static TerrainController controller;
	
	@BeforeAll
	static void setup() {
		controller = new TerrainController();
		controller.start();
		map = new IsometricTileMap(controller);
	}

	@AfterAll
	static void teardown() {
		map.dispose();
		controller.stop();
	}

	@Test
	void testInstanciateIsometricTileMap() {
		Assertions.assertThrows(RuntimeException.class, () -> new IsometricTileMap(null));
		TerrainController controller = new TerrainController();
		Assertions.assertNotNull(new IsometricTileMap(controller));
	}

	@Test
	void testCellCount() {
//		TerrainController controller = new TerrainController();
//		controller.start();
//		IsometricTileMap isometricTileMap = new IsometricTileMap(controller);
//		Assertions.assertEquals(0, isometricTileMap.countCells());
//
//		TerrainBuffer buffer = controller.getBuffer();
//		CellRef cell = buffer.getCell(10);
//		cell.setTileType(1);
//		cell.save();
//		isometricTileMap.act(0);
//		
//		Assertions.assertEquals(1, isometricTileMap.countCells());
//		
//		cell.setTileType(0);
//		cell.save();
//		isometricTileMap.act(0);
//		
//		Assertions.assertEquals(0, isometricTileMap.countCells());
//		controller.stop();
	}
}
