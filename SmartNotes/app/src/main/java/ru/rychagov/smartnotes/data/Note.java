package ru.rychagov.smartnotes.data;

public class Note {

	private String title;
	private String text;

	public Note(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}
}
