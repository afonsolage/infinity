package com.lagecompany.infinity.environment.terrain.event;

import java.io.Serializable;

import com.lagecompany.infinity.environment.terrain.TileType;

public class UpdateCell implements Serializable {
	private static final long serialVersionUID = 2950188886332297632L;
	
	private final int x;
	private final int y;
	private final int z;
	private final TileType type;

	public UpdateCell(int x, int y, int z, TileType type) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public TileType getType() {
		return type;
	}
}
