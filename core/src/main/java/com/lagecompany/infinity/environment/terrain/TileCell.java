package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class TileCell extends Actor {

    public static final int SHADOW_NE = 0;
    public static final int SHADOW_NW = 1;
    public static final int SHADOW_SE = 2;
    public static final int SHADOW_SW = 3;
    public static final int SHADOW_N = 4;
    public static final int SHADOW_S = 5;
    public static final int SHADOW_E = 6;
    public static final int SHADOW_W = 7;

    // TODO: Add other business data
    private final Sprite sprite;
    private final Sprite[] shadows = new Sprite[8];

    private final TileType type;
    private final int index;

    private float z;

    public TileCell(int index, TileType type, Sprite sprite) {
        this.index = index;
        this.type = type;
        this.sprite = sprite;

//        sprite.setAlpha(0.7f);
        calcIsometricPosition();
    }

    private void calcIsometricPosition() {
        Vector3 isometric = IsometricTileMap.toIsometric(TerrainBuffer.toPosition(index));
        z = isometric.z;
        setPosition(isometric.x, isometric.y);
    }

    public float getZ() {
        return z;
    }

    public TileType getType() {
        return type;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getIndex() {
        return index;
    }

    public void setShadow(int shadowIndex, Sprite shadowSprite) {
        if (shadowSprite != null)
            shadowSprite.setPosition(getX(), getY() + TileType.TILE_HEIGHT_OFFSET);

        shadows[shadowIndex] = shadowSprite;
    }

    @Override
    protected void positionChanged() {
        for (Sprite shadow : shadows) {
            if (shadow != null)
                shadow.setPosition(getX(), getY());
        }
        sprite.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);

        for (Sprite shadow : shadows) {
            if (shadow != null) {
                shadow.draw(batch, parentAlpha);
            }
        }
    }

    @Override
    public String toString() {
        return "TileCell [index=" + index + ", name=" + getSprite() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TileCell other = (TileCell) obj;
        return index == other.index;
    }

    public void dispose() {
    }
}
