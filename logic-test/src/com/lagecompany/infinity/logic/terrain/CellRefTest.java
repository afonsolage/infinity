package com.lagecompany.infinity.logic.terrain;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CellRefTest {

	private TerrainBuffer buffer;
	private CellRef ref;

	@BeforeEach
	void setup() {
		Random rnd = new Random();
		buffer = new TerrainBuffer();
		buffer.allocate();
		ref = buffer.get(rnd.nextInt(TerrainBuffer.WIDTH), rnd.nextInt(TerrainBuffer.HEIGHT),
				rnd.nextInt(TerrainBuffer.DEPTH));
	}

	@AfterEach
	void teardown() {
		buffer.free();
	}

	@Test
	void testClear() {
		ref.setValue(445);
		ref.clear();
		Assertions.assertEquals(0, ref.getValue());
	}

	@Test
	void testRefresh() {
		long current = ref.getValue();
		ref.setValue(-987);
		ref.refresh();
		Assertions.assertEquals(current, ref.getValue());
	}

	@Test
	void testSave() {
		long oldValue = ref.getValue();
		ref.setValue(564);
		long newValue = ref.getValue();
		ref.save();
		ref.setValue(999);
		ref.refresh();
		long currentValue = ref.getValue();
		Assertions.assertNotEquals(oldValue, currentValue);
		Assertions.assertEquals(newValue, currentValue);
	}

	@Test
	void testGetTileType() {
		ref.setValue(885471235);
		Assertions.assertEquals(2051, ref.getTileType());

		ref.setValue(1256380726);
		Assertions.assertEquals(2358, ref.getTileType());

		ref.clear();
		Assertions.assertEquals(0, ref.getTileType());

		ref.setValue(-1);
		Assertions.assertEquals(4095, ref.getTileType());
	}

	@Test
	void testSetTileType() {
		ref.clear();

		ref.setTileType(18);
		Assertions.assertEquals(18, ref.getTileType());

		ref.setTileType(192);
		Assertions.assertEquals(192, ref.getTileType());

		ref.setValue(444564);
		ref.setTileType(0);

		Assertions.assertEquals(0, ref.getTileType());
		Assertions.assertEquals(442368, ref.getValue());
	}

	@Test
	void testGetLighting() {
		ref.setValue(1238198946);
		Assertions.assertEquals(6, ref.getLighting());

		ref.setValue(756486131);
		Assertions.assertEquals(0, ref.getLighting());

		ref.setValue(49676175);
		Assertions.assertEquals(15, ref.getLighting());

		ref.clear();
		Assertions.assertEquals(0, ref.getLighting());

		ref.setValue(-1);
		Assertions.assertEquals(15, ref.getLighting());
	}

	@Test
	void testSetLighting() {
		ref.clear();

		ref.setLighting(8);
		Assertions.assertEquals(8, ref.getLighting());

		ref.setLighting(3);
		Assertions.assertEquals(3, ref.getLighting());

		ref.setValue(564564);
		ref.setLighting(0);
		Assertions.assertEquals(0, ref.getLighting());
		Assertions.assertEquals(527700, ref.getValue());
	}
}
