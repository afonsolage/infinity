package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

import java.util.ArrayList;
import java.util.function.Predicate;

public class IsometricTileMap extends Actor {
    private static final String LOG_TAG = IsometricTileMap.class.getSimpleName();

    private final TextureAtlas atlas;
    private final TerrainController controller;
    private final TileCell[] cells;

    public IsometricTileMap(TerrainController controller) {
        if (controller == null) {
            logAndThrow("Invalid controller received");
        }

        this.controller = controller;
        this.cells = new TileCell[TerrainBuffer.BUFFER_LENGTH];
        this.atlas = new TextureAtlas(Gdx.files.internal("atlas/terrain.atlas"));
    }

    private void logAndThrow(String errorMessage) {
        RuntimeException exception = new RuntimeException(errorMessage);
        Gdx.app.error(LOG_TAG, errorMessage, exception);
        throw exception;
    }

    private void updateTileCells(TerrainBuffer buffer) {
        for (int i = 0; i < TerrainBuffer.BUFFER_LENGTH; i++) {
            CellRef cell = buffer.getCell(i);

            if (cell.getTileType() == 0) {
                destroyTileCell(i);
            } else {
                updateTileCell(cell, buffer);
            }
        }
    }

    private void updateTileCell(CellRef ref, TerrainBuffer buffer) {
        TileCell cell = cells[ref.getIndex()];
        TileType type = TileType.getById(ref.getTileType());

        if (type == TileType.NONE) {
            logAndThrow("Invalid tile type: " + ref.getTileType());
        }

        // If we'll change the cell type, we must destroy the existing one first and
        // create a new one
        if (cell != null && type != cell.getType()) {
            destroyTileCell(ref.getIndex());
            cell = null;
        }

        // If there is no cell or cell was destroyed before, let's create a new one
        if (cell == null) {
            Sprite sprite = atlas.createSprite(type.toString());

            if (sprite == null) {
                logAndThrow("Failed to create tile cell. Unable to find texture with name: " + type.toString());
            }

            cell = new TileCell(ref.getIndex(), type, sprite);
            cells[ref.getIndex()] = cell;
        }

        computeShadow(ref, cell, buffer);
    }

    private void computeShadow(CellRef ref, TileCell cell, TerrainBuffer buffer) {
        Vector3 position = TerrainBuffer.toPosition(ref.getIndex());

        if (position.z == TerrainBuffer.Z_SIZE - 1)
            return;

        if (buffer.getCell((int) position.x, (int) position.y, (int) position.z + 1).getTileType() != TileType.NONE.ordinal())
            return;

        boolean sw = false;
        boolean ne = false;
        boolean se = false;
        boolean nw = false;

        //SW
        if (position.x > 0) {
            sw = addShadow(cell, buffer, position, new Vector2(-1, 0), "s_sw", TileCell.SHADOW_SW);
        }

        //NE
        if (position.x < TerrainBuffer.X_SIZE - 1) {
            ne = addShadow(cell, buffer, position, new Vector2(1, 0), "s_ne", TileCell.SHADOW_NE);
        }

        //SE
        if (position.y > 0) {
            se = addShadow(cell, buffer, position, new Vector2(0, -1), "s_se", TileCell.SHADOW_SE);
        }

        //NW
        if (position.y < TerrainBuffer.Y_SIZE - 1) {
            nw = addShadow(cell, buffer, position, new Vector2(0, 1), "s_nw", TileCell.SHADOW_NW);
        }

        //W
        if (!sw && !nw && position.x > 0 && position.y < TerrainBuffer.Y_SIZE - 1) {
            addShadow(cell, buffer, position, new Vector2(-1, 1), "s_w", TileCell.SHADOW_W);
        }

        //N
        if (!ne && !nw && position.x < TerrainBuffer.X_SIZE -1 && position.y < TerrainBuffer.Y_SIZE - 1) {
            addShadow(cell, buffer, position, new Vector2(1, 1), "s_n", TileCell.SHADOW_N);
        }

        //E
        if (!ne && !se && position.x < TerrainBuffer.X_SIZE -1 && position.y > 0) {
            addShadow(cell, buffer, position, new Vector2(1, -1), "s_e", TileCell.SHADOW_E);
        }

        //S
        if (!sw && !se && position.x > 0 && position.y > 0) {
            addShadow(cell, buffer, position, new Vector2(-1, -1), "s_s", TileCell.SHADOW_S);
        }
    }

    private boolean addShadow(TileCell cell, TerrainBuffer buffer, Vector3 position, Vector2 addPos, String shadowName, int shadowIndex) {
        int neighborHeight = 0;
        for (int z = (int) position.z + 1; z < TerrainBuffer.Z_SIZE; z++) {
            CellRef neighbor = buffer.getCell((int) (position.x + addPos.x), (int) (position.y + addPos.y), z);

            if (neighbor.getTileType() == TileType.NONE.ordinal())
                break;
            else
                neighborHeight = z;
        }

        if (neighborHeight > 0) {
            float transparency = neighborHeight * 0.25f;
            Sprite sprite = atlas.createSprite(shadowName);
            sprite.setAlpha(transparency);
            cell.setShadow(shadowIndex, sprite);
            return  true;
        } else {
            return false;
        }
    }

    private void destroyTileCell(int index) {
        if (cells[index] != null) {
            cells[index].dispose();
            cells[index] = null;
        }
    }

    public int countCells() {
        return getCells(c -> true).size();
    }

    public ArrayList<TileCell> getCells(Predicate<TileCell> predicate) {
        ArrayList<TileCell> list = new ArrayList<>();
        for (TileCell cell : cells) {
            if (cell != null && predicate.test(cell))
                list.add(cell);
        }
        return list;
    }

    public TileCell findCell(Predicate<TileCell> predicate) {
        for (TileCell cell : cells) {
            if (cell != null && predicate.test(cell))
                return cell;
        }
        return null;
    }

    public void dispose() {
        for (TileCell cell : cells) {
            if (cell != null)
                cell.dispose();
        }
        atlas.dispose();
    }

    @Override
    public void act(float delta) {
        if (controller.isBufferDirt()) {
            updateTileCells(controller.getBuffer());
            controller.cleanBufferDirtiness();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int z = 0; z < TerrainBuffer.Z_SIZE; z++) {
            for (int x = TerrainBuffer.X_SIZE - 1; x >= 0; x--) {
                for (int y = TerrainBuffer.Y_SIZE - 1; y >= 0; y--) {
                    int index = TerrainBuffer.toIndex(x, y, z);

                    if (cells[index] == null)
                        continue;

                    cells[index].draw(batch, parentAlpha);
                }
            }
        }
    }

    public static Vector3 toIsometric(float x, float y, float z) {
        return toIsometric(new Vector3(x, y, z));
    }

    public static Vector3 toIsometric(Vector3 pos) {
        float x = pos.x;
        float y = pos.y;

        pos.x = (x - y) * TileType.TILE_WIDTH_HALF;
        pos.y = (x + y) * TileType.TILE_HEIGHT_HALF + (pos.z * TileType.TILE_HEIGHT_OFFSET);
        return pos;
    }
}
