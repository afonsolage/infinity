package com.lagecompany.infinity.logic.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

class TerrainBufferTest {

	@Test
	void testToIndex() {
		Assertions.assertEquals(0, TerrainBuffer.toIndex(0, 0, 0));

		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(-1, 0, 0));
		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(0, -1, TerrainBuffer.DEPTH));
		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(0, TerrainBuffer.HEIGHT, 0));

		Assertions.assertEquals(TerrainBuffer.BUFFER_LENGTH - 1,
				TerrainBuffer.toIndex(TerrainBuffer.WIDTH - 1, TerrainBuffer.HEIGHT - 1, TerrainBuffer.DEPTH - 1));

		int expected = TerrainBuffer.BUFFER_LENGTH - 1;
		for (int x = TerrainBuffer.WIDTH - 1; x >= 0; x--) {
			for (int y = TerrainBuffer.HEIGHT - 1; y >= 0; y--) {
				for (int z = TerrainBuffer.DEPTH - 1; z >= 0; z--) {
					Assertions.assertEquals(expected--, TerrainBuffer.toIndex(x, y, z));
				}
			}
		}
	}

	@Test
	void testToPosition() {
		Assertions.assertEquals(new Vector3(0, 0, 0), TerrainBuffer.toPosition(0));

		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toPosition(-1));
		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toPosition(TerrainBuffer.BUFFER_LENGTH));

		Assertions.assertEquals(new Vector3(TerrainBuffer.WIDTH - 1, TerrainBuffer.HEIGHT - 1, TerrainBuffer.DEPTH - 1),
				TerrainBuffer.toPosition(TerrainBuffer.BUFFER_LENGTH - 1));

		for (int i = 0; i < TerrainBuffer.BUFFER_LENGTH; i++) {
			Vector3 position = TerrainBuffer.toPosition(i);
			Assertions.assertTrue(position.x == (i / TerrainBuffer.HEIGHT / TerrainBuffer.DEPTH)
					&& position.y == (i / TerrainBuffer.DEPTH) && position.z == i % TerrainBuffer.DEPTH);
		}
	}
}
