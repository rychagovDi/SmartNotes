package ru.rychagov.smartnotes.data;

public enum Priority {
	NONE(0),
	LOW(1),
	MEDIUM(2),
	HIGH(3);

	private int value;

	Priority(int priority) {
		value = priority;
	}

	public static Priority fromInt(int priority) {

		for (Priority p : values()) {
			if (p.getInt() == priority) {
				return p;
			}
		}

		return NONE;
	}

	public int getInt() {
		return value;
	}
}


