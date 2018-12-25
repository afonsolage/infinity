package com.lagecompany.infinity.rx.worker;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.lagecompany.infinity.InfinityGame;

import io.reactivex.Scheduler.Worker;
import io.reactivex.disposables.Disposable;

public class MainWorker extends Worker {
	
	class Task implements Disposable, Runnable {
		private final Runnable originalTask;
		private volatile boolean disposed = false;

		public Task(Runnable task) {
			this.originalTask = task;
		}

		@Override
		public void run() {
			if (!disposed)
				originalTask.run();
		}

		@Override
		public void dispose() {
			disposed = true;
		}

		@Override
		public boolean isDisposed() {
			return disposed;
		}
	}

	private volatile boolean disposed = false;

	@Override
	public void dispose() {
		disposed = true;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
		Task task = new Task(run);

		if (delay > 0) {
			long scheduled = System.currentTimeMillis() + unit.toMillis(delay);
			Gdx.app.postRunnable(() -> {
				InfinityGame.scheduleTask(scheduled, task);
			});
		} else {
			Gdx.app.postRunnable(task);
		}
		return task;
	}

}
