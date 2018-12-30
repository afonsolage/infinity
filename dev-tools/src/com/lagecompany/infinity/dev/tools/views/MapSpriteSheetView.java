package com.lagecompany.infinity.dev.tools.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.lagecompany.infinity.dev.tools.DevGame;

public class MapSpriteSheetView extends AbstractLmlView {

	private static final long R_MASK = 0xFF000000L;
	private static final long G_MASK = 0x00FF0000L;
	private static final long B_MASK = 0x0000FF00L;
	private static final long A_MASK = 0x000000FFL;
	private static final long RGBA_MASK = R_MASK | B_MASK | G_MASK | A_MASK;

	class IntPair {
		public int x;
		public int y;

		public IntPair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
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
			IntPair other = (IntPair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		private MapSpriteSheetView getOuterType() {
			return MapSpriteSheetView.this;
		}
	}

	public static String ID = "map-sprite-sheet-view";

	@LmlActor("spriteSheetPath")
	VisTextField spriteSheetPath;

	public MapSpriteSheetView() {
		super(DevGame.newStage());
	}

	@Override
	public FileHandle getTemplateFile() {
		return Gdx.files.internal("views/map-sprite-sheet-view.lml");
	}

	@Override
	public String getViewId() {
		return ID;
	}

	@LmlAction("processSpriteSheet")
	public void processSpriteSheet() {
		FileHandle handle = Gdx.files.internal(spriteSheetPath.getText());
		Texture texture = new Texture(handle);

		List<TextureRegion> regions = detectSpriteSheetRegions(texture);

		DevGame game = DevGame.getInstance();

		MapSpriteView.texture = texture;
		MapSpriteView.regions = regions;
		MapSpriteView.texturePath = spriteSheetPath.getText();

		game.setView(MapSpriteView.class);
	}

	private List<TextureRegion> detectSpriteSheetRegions(Texture texture) {
		List<TextureRegion> result = new ArrayList<>();

		TextureData data = texture.getTextureData();
		data.prepare();
		Pixmap pixmap = data.consumePixmap();

		Set<IntPair> flooded = new HashSet<>();

		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				IntPair pair = new IntPair(x, y);

				if (flooded.contains(pair))
					continue;

				if (isTransparent(pixmap, x, y))
					continue;

				long pixel = getPixel(pixmap, x, y);

				TextureRegion region = calculateTextureRegion(pixel, pair, flooded, texture, pixmap);
				result.add(region);
			}
		}

		result.sort((a, b) -> (a.getRegionX() + (a.getRegionY() / 80) * 1000)
				- (b.getRegionX() + (b.getRegionY() / 80) * 1000));

		return result;
	}

	private boolean isTransparent(Pixmap pixmap, int x, int y) {
		return getAlpha(getPixel(pixmap, x, y)) == 0;
	}

	private TextureRegion calculateTextureRegion(long pixel, IntPair position, Set<IntPair> flooded, Texture texture,
			Pixmap pixmap) {

		int minX = position.x;
		int maxX = position.x;
		int minY = position.y;
		int maxY = position.y;

		Queue<IntPair> queue = new Queue<>();

		queue.addFirst(position);

		while (!queue.isEmpty()) {
			IntPair current = queue.removeFirst();

			if (flooded.contains(current))
				continue;

			flooded.add(current);

			if (isTransparent(pixmap, current.x, current.y))
				continue;

			if (current.x < minX)
				minX = current.x;
			else if (current.x > maxX)
				maxX = current.x;

			if (current.y < minY)
				minY = current.y;
			else if (current.y > maxY)
				maxY = current.y;

			queue.addLast(new IntPair(current.x + 1, current.y));
			queue.addLast(new IntPair(current.x - 1, current.y));
			queue.addLast(new IntPair(current.x, current.y + 1));
			queue.addLast(new IntPair(current.x, current.y - 1));
		}

		return new TextureRegion(texture, minX, minY, maxX - minX, maxY - minY);
	}

	private int getAlpha(long pixel) {
		return (int) (pixel & A_MASK);
	}

	private long getPixel(Pixmap pixmap, int x, int y) {
		return (long) pixmap.getPixel(x, y) & RGBA_MASK;
	}

}
