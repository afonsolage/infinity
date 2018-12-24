package com.lagecompany.infinity.logic.terrain;

public class CellRef {

	public static final int TILE_TYPE_BEGIN_SHIFT = 0;
	public static final int TILE_TYPE_END_SHIFT = 12;
	public static final int TILE_TYPE_MASK = 0xFFF;

	public static final int LIGHTING_BEGIN_SHIT = 12;
	public static final int LIGHTING_END_SHIFT = 16;
	public static final int LIGHTING_MASK = 0xF000;

	// TODO: Add other infos

	private final int index;
	private final TerrainBuffer buffer;

	private long value;

	CellRef(TerrainBuffer buffer, int index) {
		this.buffer = buffer;
		this.index = index;

		refresh();
	}

	public int getIndex() {
		return index;
	}
	
	public long getValue() {
		return value;
	}

	public void setValue(long newValue) {
		this.value = newValue;
	}

	public void refresh() {
		this.value = buffer.get(index);
	}

	public void clear() {
		this.value = 0;
	}

	public void save() {
		this.buffer.set(index, value);
	}

	public int getTileType() {
		return (int) ((value & TILE_TYPE_MASK) >> TILE_TYPE_BEGIN_SHIFT);
	}

	public void setTileType(int type) {
		value &= ~TILE_TYPE_MASK; // Clear existing value
		value |= (type << TILE_TYPE_BEGIN_SHIFT) & TILE_TYPE_MASK;
	}

	public int getLighting() {
		return (int) ((value & LIGHTING_MASK) >> LIGHTING_BEGIN_SHIT);
	}

	public void setLighting(int lighting) {
		value &= ~LIGHTING_MASK; // Clear existing value
		value |= (lighting << LIGHTING_BEGIN_SHIT) & LIGHTING_MASK;
	}
}
