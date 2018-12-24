package com.lagecompany.infinity.logic.terrain;

import com.badlogic.gdx.math.Vector3;

/**
 * TerrainBuffer is a class that represents the current "view" of the terrain.
 * This is what player will see rendered at screen.
 * 
 * @author afonsolage
 *
 */
public class TerrainBuffer {

	/**
	 * X-axis length of buffer
	 */
	public static final int WIDTH = 20;

	/**
	 * Y-axis length of buffer
	 */
	public static final int HEIGHT = 4;

	/**
	 * Z-axis length of buffer
	 */
	public static final int DEPTH = 20;

	/**
	 * Internal buffer size. Is calculated using WIDTH * HEIGHT * DEPTH
	 */
	public static final int BUFFER_LENGTH = WIDTH * HEIGHT * DEPTH;

	// The access modified should default since we'r gonna use it on test classes
	// also
	static final int X_UNIT = HEIGHT * DEPTH;
	static final int Y_UNIT = DEPTH;
	static final int Z_UNIT = 1;

	public static int toIndex(int x, int y, int z) {
		checkBounds(x, y, z);
		return (x * X_UNIT) + (y * Y_UNIT) + (z * Z_UNIT);
	}

	public static Vector3 toPosition(int index) {
		checkBounds(index);

		return new Vector3(index / X_UNIT, (index % TerrainBuffer.X_UNIT) / Y_UNIT, (index % Y_UNIT) / Z_UNIT);
	}

	private static void checkBounds(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= WIDTH || y >= HEIGHT || z >= DEPTH)
			throw new RuntimeException("Invalid bounds: " + x + ", " + y + ", " + z);
	}

	private static void checkBounds(int index) {
		if (index < 0 || index >= BUFFER_LENGTH)
			throw new RuntimeException("Invalid index: " + index);
	}

	private long[] buffer;

	public void allocate() {
		buffer = new long[BUFFER_LENGTH];
	}

	public void free() {
		// TODO: Maybe implement some pooling for those buffers?
		buffer = null;
	}

	public boolean isAllocated() {
		return buffer != null;
	}

	long get(int index) {
		checkBounds(index);

		return buffer[index];
	}

	void set(int index, long value) {
		checkBounds(index);

		buffer[index] = value;
	}
	
	public CellRef get(int x, int y, int z) {
		return new CellRef(this, toIndex(x, y, z));
	}
}
