package com.lagecompany.infinity;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.lagecompany.infinity.input.InputObserver;
import com.lagecompany.infinity.stage.AbstractStage;
import com.lagecompany.infinity.stage.GameStage;
import com.lagecompany.infinity.stage.StageType;

import gnu.trove.map.hash.TLongObjectHashMap;

public class InfinityGame extends ApplicationAdapter {

	private static final int MAX_TASKS_PER_FRAME = 10;

	private AbstractStage currentStage;
	private static TLongObjectHashMap<Runnable> scheduledTasks = new TLongObjectHashMap<>();

	public static void scheduleTask(long when, Runnable task) {
		scheduledTasks.put(when, task);
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		InputObserver.init();

		changeStage(StageType.Game);
	}

	@Override
	public void resize(int width, int height) {
		currentStage.getViewport().update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		checkScheduledTasks();

		currentStage.act();
		currentStage.draw();
	}

	private long[] executedTasks = new long[MAX_TASKS_PER_FRAME];

	private void checkScheduledTasks() {
		long now = System.currentTimeMillis();

		int executedCount = 0;

		for (long when : scheduledTasks.keys()) {
			if (when <= now) {
				scheduledTasks.get(when).run();
				executedTasks[executedCount++] = when;
				if (executedCount == executedTasks.length)
					break;
			}
		}

		for (long key : executedTasks) {
			scheduledTasks.remove(key);
		}
	}

	@Override
	public void dispose() {
	}

	public void changeStage(StageType type) {
		AbstractStage stage;
		switch (type) {
		case Game: {
			stage = new GameStage(this);
			break;
		}
		default: {
			throw new UnsupportedOperationException("Not implemented yet.");
		}
		}

		if (currentStage != null)
			currentStage.finalize();

		currentStage = stage;
		currentStage.initialize();
	}
}
