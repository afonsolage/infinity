package com.lagecompany.infinity.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.lagecompany.infinity.input.InputEvent.KeyDown;
import com.lagecompany.infinity.input.InputEvent.KeyTyped;
import com.lagecompany.infinity.input.InputEvent.KeyUp;
import com.lagecompany.infinity.input.InputEvent.MouseMoved;
import com.lagecompany.infinity.input.InputEvent.Scrolled;
import com.lagecompany.infinity.input.InputEvent.TouchDown;
import com.lagecompany.infinity.input.InputEvent.TouchDragged;
import com.lagecompany.infinity.input.InputEvent.TouchUp;
import com.lagecompany.infinity.rx.GdxSchedulers;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class InputObserver implements InputProcessor {
	private static final String LOG_TAG = InputObserver.class.getSimpleName();

	private static InputObserver instance;
	private Subject<InputEvent> subject;

	private InputObserver() {
		subject = PublishSubject.<InputEvent>create().toSerialized();
	}

	public static void init() {
		instance = new InputObserver();
		Gdx.input.setInputProcessor(instance);
	}

	public static io.reactivex.disposables.Disposable onKeyUp(KeyUp consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.KEY_UP)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event key up", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onKeyDown(KeyDown consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.KEY_DOWN)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event key down", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onKeyTyped(KeyTyped consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.KEY_TYPED)
				.subscribe(evt -> {
					try {
						consumer.run((char) evt.getData()[0]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event key typed", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onMouseMoved(MouseMoved consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.MOUSE_MOVED)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0], evt.getData()[1]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event mouse moved", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onScrolled(Scrolled consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.SCROLLED)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event scrolled", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onTouchDown(TouchDown consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.TOUCH_DOWN)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0], evt.getData()[1], evt.getData()[2], evt.getData()[3]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event touch down", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onTouchUp(TouchUp consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.TOUCH_UP)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0], evt.getData()[1], evt.getData()[2], evt.getData()[3]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event touch up", e);
					}
				});
	}

	public static io.reactivex.disposables.Disposable onTouchDragged(TouchDragged consumer) {
		return instance.subject.observeOn(GdxSchedulers.main()).filter(evt -> evt.getType() == InputType.TOUCH_DRAGGED)
				.subscribe(evt -> {
					try {
						consumer.run(evt.getData()[0], evt.getData()[1], evt.getData()[2]);
					} catch (Exception e) {
						Gdx.app.error(LOG_TAG, "Failed to process event touch dragged", e);
					}
				});
	}

	@Override
	public boolean keyDown(int keycode) {
		subject.onNext(new InputEvent(InputType.KEY_DOWN, keycode));
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		subject.onNext(new InputEvent(InputType.KEY_UP, keycode));
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		subject.onNext(new InputEvent(InputType.KEY_TYPED, character));
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		subject.onNext(new InputEvent(InputType.TOUCH_DOWN, screenX, screenY, pointer, button));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		subject.onNext(new InputEvent(InputType.TOUCH_UP, screenX, screenY, pointer, button));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		subject.onNext(new InputEvent(InputType.TOUCH_DRAGGED, screenX, screenY, pointer));
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		subject.onNext(new InputEvent(InputType.MOUSE_MOVED, screenX, screenY));
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		subject.onNext(new InputEvent(InputType.SCROLLED, amount));
		return false;
	}

}
