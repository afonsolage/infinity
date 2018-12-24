package com.lagecompany.infinity.logic.terrain;

import com.badlogic.gdx.math.Vector3;

/**
 * TerrainBuffer is a class that represents the current "view" of the terrain.
 * This is what player will see rendered at screen. This class use Z as up
 * vector.
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
	public static final int HEIGHT = 5;

	/**
	 * Z-axis length of buffer
	 */
	public static final int DEPTH = 20;

	/**
	 * Internal buffer size. Is calculated using WIDTH * HEIGHT * DEPTH
	 */
	public static final int BUFFER_LENGTH = WIDTH * HEIGHT * DEPTH;

	/**
	 * Size of X axis on TerrainBuffer
	 */
	public static final int X_SIZE = WIDTH;

	/**
	 * Size of Y axis on TerrainBuffer
	 */
	public static final int Y_SIZE = DEPTH;

	/**
	 * Size of Z axis on TerrainBuffer
	 */
	public static final int Z_SIZE = HEIGHT;

	/**
	 * The size of a single Z unit. This returns the number of indices that is
	 * increased by advancing Z by 1.
	 */
	public static final int Z_UNIT = X_SIZE * Y_SIZE;

	/**
	 * The size of a single X unit. This returns the number of indices that is
	 * increased by advancing X by 1.
	 */
	public static final int X_UNIT = Y_SIZE;

	/**
	 * The size of a single Y unit. This returns the number of indices that is
	 * increased by advancing Y by 1.
	 */
	public static final int Y_UNIT = 1;

	public TerrainBuffer() {

	}

	public TerrainBuffer(boolean allocate) {
		this();
		if (allocate) {
			this.allocate();
		}
	}

	public static int toIndex(int x, int y, int z) {
		checkBounds(x, y, z);
		return (x * X_UNIT) + (y * Y_UNIT) + (z * Z_UNIT);
	}

	public static Vector3 toPosition(int index) {
		checkBounds(index);

		return new Vector3((index % TerrainBuffer.Z_UNIT) / X_UNIT, index % X_UNIT, index / Z_UNIT);
	}

	private static void checkBounds(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= X_SIZE || y >= Y_SIZE || z >= Z_SIZE)
			throw new RuntimeException("Invalid bounds: " + x + ", " + y + ", " + z);
	}

	private static void checkBounds(int index) {
		if (index < 0 || index >= BUFFER_LENGTH)
			throw new RuntimeException("Invalid index: " + index);
	}

	private long[] buffer;
	private boolean dirt = false;

	public boolean isDirt() {
		return dirt;
	}

	public void setDirt(boolean dirt) {
		this.dirt = dirt;
	}

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

	public CellRef getCell(int x, int y, int z) {
		return getCell(toIndex(x, y, z));
	}

	public CellRef getCell(int index) {
		return new CellRef(this, index);
	}
}
