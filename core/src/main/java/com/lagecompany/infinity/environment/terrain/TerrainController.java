package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.lagecompany.infinity.environment.terrain.event.EventData;
import com.lagecompany.infinity.environment.terrain.event.EventType;
import com.lagecompany.infinity.environment.terrain.event.UpdateCell;
import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class TerrainController implements Disposable {

	private static final String LOG_TAG = TerrainController.class.getSimpleName();

	private final Subject<EventData> subject;
	private final io.reactivex.disposables.Disposable subscription;

	private final TerrainBuffer swapBuffer;
	private final TerrainBuffer frontBuffer;
	private final TerrainBuffer backBuffer;

	public TerrainController() {
		swapBuffer = new TerrainBuffer(true);
		frontBuffer = new TerrainBuffer(true);
		backBuffer = new TerrainBuffer(true);

		subject = PublishSubject.<EventData>create().toSerialized();

		subscription = subject.observeOn(Schedulers.computation()).subscribe(this::processEvent, this::onError, this::onComplete);
	}

	public void init() {
		publishEvent(new EventData(EventType.INIT));
	}

	public void publishEvent(EventData event) {
		subject.onNext(event);
	}

	private void processEvent(EventData event) {
		Gdx.app.debug(LOG_TAG, "Processing event: " + event.getType());

		switch (event.getType()) {
		case INIT:
			generateTerrain();
			break;
		case UPDATE_CELL:
			updateCell(event.getData());
			break;
		default:
			Gdx.app.debug(LOG_TAG, "Terrain event not supported: " + event.getType());
			throw new UnsupportedOperationException("Terrain Event " + event.getType());
		}
	}

	private void onError(Throwable t) {
		Gdx.app.error(LOG_TAG, "Unhandled exception at Terrain Controller: " + t.getMessage());
	}
	
	private void onComplete() {
		Gdx.app.debug(LOG_TAG, "Terrain Controller completed.");
	}
	
	private void updateCell(UpdateCell data) {
		CellRef cell = backBuffer.getCell(data.getX(), data.getY(), data.getZ());
		cell.setTileType(data.getType().ordinal());
		cell.save();

		swapBackBuffer();
	}

	public TerrainBuffer getBuffer() {
		return frontBuffer;
	}

	private void generateTerrain() {
		for (int x = 0; x < TerrainBuffer.X_SIZE; x++) {
			for (int y = 0; y < TerrainBuffer.Y_SIZE; y++) {
				CellRef cell = backBuffer.getCell(x, y, 2);
				cell.setTileType(
						x == 0 || y == 0 || x == TerrainBuffer.X_SIZE - 1 || y == TerrainBuffer.Y_SIZE - 1 ? 3 : 1);

				cell.save();
			}
		}

		publishEvent(new EventData(EventType.UPDATE_CELL, new UpdateCell(0, 0, 1, TileType.DIRT)));

		swapBackBuffer();
	}

	public boolean isBufferDirt() {
		if (frontBuffer.isDirt()) {
			return true;
		} else if (swapBuffer.isDirt()) {
			swapFrontBuffer();
			return true;
		} else {
			return false;
		}
	}

	public void cleanBufferDirtiness() {
		frontBuffer.setDirt(false);
	}

	private void swapBackBuffer() {
		synchronized (swapBuffer) {
			swapBuffer.copy(backBuffer);
			swapBuffer.setDirt(true);
			backBuffer.setDirt(false);
		}
	}

	private void swapFrontBuffer() {
		synchronized (swapBuffer) {
			frontBuffer.copy(swapBuffer);
			frontBuffer.setDirt(true);
			swapBuffer.setDirt(false);
		}
	}

	@Override
	public void dispose() {
		subject.onComplete();
		subscription.dispose();
	}
}
