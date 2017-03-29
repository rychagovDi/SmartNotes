package ru.rychagov.smartnotes.data;

import android.util.Log;

public class Note {

	private static final String TAG = "Note";

	private int id;
	private String title;
	private String text;
	private long time;
	private Priority priority;

	public Note(int id, String title, String text, long time, Priority priority) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.time = time;
		this.priority = priority;

		Log.i(TAG, String.format("Note was created. %s", this));
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public long getTime() {
		return time;
	}

	public Priority getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		return String.format("Title: %s. Timestamp: %s", title, time);
	}
}
