package ru.rychagov.smartnotes.data;

import android.util.Log;

public class Note {

	private static final String TAG = "Note";

	private String title;
	private String text;
	private long time;

	public Note(String title, String text, long time) {
		this.title = title;
		this.text = text;
		this.time = time;

		Log.i(TAG, String.format("Note was created. %s", this));
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

	@Override
	public String toString() {
		return String.format("Title: %s. Timestamp: %s", title, time);
	}
}
