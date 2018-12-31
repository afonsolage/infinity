package com.lagecompany.infinity.rx;

import com.lagecompany.infinity.rx.worker.MainWorker;

import io.reactivex.Scheduler;

public class GdxSchedulers {

	private static MainWorker mainWorker = new MainWorker();
	
	public static Scheduler main() {
		return new Scheduler() {
			@Override
			public Worker createWorker() {
				return mainWorker;
			}
		};
	}
}
