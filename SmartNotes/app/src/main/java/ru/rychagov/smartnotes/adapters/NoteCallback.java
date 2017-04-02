package ru.rychagov.smartnotes.adapters;

public interface NoteCallback {

	/**
	 * Метод, который вызывается при нажатии на заметку в списке заметок
	 */
	void onNoteClick(int position);
}
