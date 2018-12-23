package com.lagecompany.infinity;

import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;
import com.lagecompany.infinity.environment.terrain.IsometricTileMap;

public class IsometricTileMapTest extends InfinityTest {

	private static IsometricTileMap map;

	@BeforeAll
	static void setup() {
		map = new IsometricTileMap();
	}

	@AfterAll
	static void teardown() {
		map.dispose();
	}

	@Test
	void testInstanciateIsometricTileMap() {
		new IsometricTileMap();
	}

	@Test
	void testToIndex() {
		int expected = 0;
		for (int y = 0; y < IsometricTileMap.HEIGHT; y++) {
			for (int x = 0; x < IsometricTileMap.WIDTH; x++) {
				Assertions.assertEquals(expected++, IsometricTileMap.toIndex(x, y));
			}
		}
	}

	@Test
	void testToPosition() {
		int total = IsometricTileMap.WIDTH * IsometricTileMap.HEIGHT;

		for (int i = 0; i < total; i++) {
			Vector2 position = IsometricTileMap.toPosition(i);
			Assertions.assertTrue(position.x == i % IsometricTileMap.WIDTH && position.y == i / IsometricTileMap.WIDTH);
		}
	}

	@Test
	void testCellCount() {
		IsometricTileMap isometricTileMap = new IsometricTileMap();
		Assertions.assertEquals(IsometricTileMap.WIDTH * IsometricTileMap.HEIGHT, isometricTileMap.getCellCount());
	}

	@Test
	void testCreateCellAt() {
		Assertions.assertThrows(RuntimeException.class, () -> {
			map.createCellAt(0, 0);
		});
		int count = map.getCellCount();
		map.createCellAt(-1, -1);
		map.createCellAt(IsometricTileMap.WIDTH, IsometricTileMap.HEIGHT);
		Assertions.assertEquals(count + 2, map.getCellCount());
	}
	
	@Test
	void testDestroyCellAt() {
		int count = map.getCellCount();
		Random random = new Random();
		map.destroyCellAt(random.nextInt(IsometricTileMap.WIDTH), random.nextInt(IsometricTileMap.HEIGHT));
		map.destroyCellAt(random.nextInt(IsometricTileMap.WIDTH), random.nextInt(IsometricTileMap.HEIGHT));
		Assertions.assertEquals(count - 2, map.getCellCount());
	}
}
