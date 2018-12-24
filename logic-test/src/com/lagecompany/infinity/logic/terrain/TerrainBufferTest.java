package com.lagecompany.infinity.logic.terrain;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

class TerrainBufferTest {

	@Test
	void testToIndex() {
		Assertions.assertEquals(0, TerrainBuffer.toIndex(0, 0, 0));

		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(-1, 0, 0));
		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(0, -1, TerrainBuffer.Z_SIZE));
		Assertions.assertThrows(RuntimeException.class, () -> TerrainBuffer.toIndex(0, TerrainBuffer.Y_SIZE, 0));

		Assertions.assertEquals(TerrainBuffer.BUFFER_LENGTH - 1,
				TerrainBuffer.toIndex(TerrainBuffer.X_SIZE - 1, TerrainBuffer.Y_SIZE - 1, TerrainBuffer.Z_SIZE - 1));

		int expected = TerrainBuffer.BUFFER_LENGTH - 1;
		for (int z = TerrainBuffer.Z_SIZE - 1; z >= 0; z--) {
			for (int x = TerrainBuffer.X_SIZE - 1; x >= 0; x--) {
				for (int y = TerrainBuffer.Y_SIZE - 1; y >= 0; y--) {
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

		Assertions.assertEquals(
				new Vector3(TerrainBuffer.X_SIZE - 1, TerrainBuffer.Y_SIZE - 1, TerrainBuffer.Z_SIZE - 1),
				TerrainBuffer.toPosition(TerrainBuffer.BUFFER_LENGTH - 1));

		Assertions.assertEquals(new Vector3(1, 1, 1),
				TerrainBuffer.toPosition(TerrainBuffer.X_UNIT + TerrainBuffer.Y_UNIT + TerrainBuffer.Z_UNIT));

		Assertions.assertEquals(new Vector3(1, 0, 0), TerrainBuffer.toPosition(TerrainBuffer.X_UNIT));

		for (int i = 0; i < TerrainBuffer.BUFFER_LENGTH; i++) {
			Vector3 position = TerrainBuffer.toPosition(i);
			Assertions.assertTrue(position.x == (i % TerrainBuffer.Z_UNIT) / TerrainBuffer.X_UNIT
					&& position.y == i % TerrainBuffer.X_UNIT && position.z == i / TerrainBuffer.Z_UNIT);
		}
	}

	@Test
	void testAllocate() {
		TerrainBuffer buffer = new TerrainBuffer();
		Assertions.assertFalse(buffer.isAllocated());
		buffer.allocate();
		Assertions.assertTrue(buffer.isAllocated());
	}

	@Test
	void testFree() {
		TerrainBuffer buffer = new TerrainBuffer();
		Assertions.assertFalse(buffer.isAllocated());
		buffer.free();
		Assertions.assertFalse(buffer.isAllocated());
		buffer.allocate();
		Assertions.assertTrue(buffer.isAllocated());
		buffer.free();
		Assertions.assertFalse(buffer.isAllocated());
	}

	@Test
	void testGet() {
		TerrainBuffer buffer = new TerrainBuffer();
		Assertions.assertThrows(RuntimeException.class, () -> buffer.get(-1));
		Assertions.assertThrows(RuntimeException.class, () -> buffer.get(TerrainBuffer.BUFFER_LENGTH));
		Assertions.assertThrows(NullPointerException.class, () -> buffer.get(0));

		buffer.allocate();

		Assertions.assertEquals(0, buffer.get(0));
		Assertions.assertEquals(0, buffer.get(new Random().nextInt(TerrainBuffer.BUFFER_LENGTH)));
		Assertions.assertEquals(0, buffer.get(TerrainBuffer.BUFFER_LENGTH - 1));
	}

	@Test
	void testSet() {
		TerrainBuffer buffer = new TerrainBuffer();

		Assertions.assertThrows(RuntimeException.class, () -> buffer.set(-1, 10));
		Assertions.assertThrows(RuntimeException.class, () -> buffer.set(TerrainBuffer.BUFFER_LENGTH, 10));
		Assertions.assertThrows(NullPointerException.class, () -> buffer.set(0, 10));

		buffer.allocate();

		buffer.set(0, 999);
		Assertions.assertEquals(999, buffer.get(0));

		int index = new Random().nextInt(TerrainBuffer.BUFFER_LENGTH);
		buffer.set(index, 1299);
		Assertions.assertEquals(1299, buffer.get(index));

		buffer.set(TerrainBuffer.BUFFER_LENGTH - 1, -987);
		Assertions.assertEquals(-987, buffer.get(TerrainBuffer.BUFFER_LENGTH - 1));
	}

	@Test
	void testGetCellRef() {
		TerrainBuffer buffer = new TerrainBuffer();

		Assertions.assertThrows(RuntimeException.class, () -> buffer.getCell(-1, 0, 0));
		Assertions.assertThrows(RuntimeException.class, () -> buffer.getCell(-1, 0, TerrainBuffer.Z_SIZE));
		Assertions.assertThrows(RuntimeException.class, () -> buffer.getCell(0, 0, TerrainBuffer.Z_SIZE));
		Assertions.assertThrows(NullPointerException.class, () -> buffer.getCell(0, 0, 0));

		buffer.allocate();

		Assertions.assertNotNull(buffer.getCell(0, 0, 0));

		Random rnd = new Random();
		for (int i = 0; i < 500; i++)
			Assertions.assertNotNull(buffer.getCell(rnd.nextInt(TerrainBuffer.X_SIZE),
					rnd.nextInt(TerrainBuffer.Y_SIZE), rnd.nextInt(TerrainBuffer.Z_SIZE)));

		Assertions.assertNotNull(
				buffer.getCell(TerrainBuffer.X_SIZE - 1, TerrainBuffer.Y_SIZE - 1, TerrainBuffer.Z_SIZE - 1));
	}
}
