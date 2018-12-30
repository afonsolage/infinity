package com.lagecompany.infinity.input;

public class InputEvent {

	private final InputType type;
	private final int[] data;

	public InputEvent(InputType type, int... data) {
		this.type = type;
		this.data = data;
	}

	public InputType getType() {
		return type;
	}

	public int[] getData() {
		return data;
	}

	public static interface InputEventConsumer {
	}

	public static interface KeyDown extends InputEventConsumer {
		void run(int keycode);
	}

	public static interface KeyUp extends InputEventConsumer {
		void run(int keycode);
	}

	public static interface KeyTyped extends InputEventConsumer {
		void run(char character);
	}

	public static interface TouchDown extends InputEventConsumer {
		void run(int screenX, int screenY, int pointer, int button);
	}

	public static interface TouchUp extends InputEventConsumer {
		void run(int screenX, int screenY, int pointer, int button);
	}

	public static interface TouchDragged extends InputEventConsumer {
		void run(int screenX, int screenY, int pointer);
	}

	public static interface MouseMoved extends InputEventConsumer {
		void run(int screenX, int screenY);
	}

	public static interface Scrolled extends InputEventConsumer {
		void run(int amount);
	}
}
