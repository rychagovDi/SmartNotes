package ru.rychagov.smartnotes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;

import static ru.rychagov.smartnotes.database.DataBaseHelper.NotesEntry;

public class DataBaseUtils {

	/**
		Обращается к базе данных и получает из нее список заметок
	 */
	public static ArrayList<Note> getNotes(Context context) {
		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

		Cursor cursor = db.query(NotesEntry.TABLE_NAME, null, null, null, null, null, null);

		ArrayList<Note> notes = new ArrayList<>();

		if (cursor != null) {
			cursor.moveToPosition(-1);

			while (cursor.moveToFirst()) {
				int id = cursor.getInt(cursor.getColumnIndex(NotesEntry._ID));
				String title = cursor.getString(cursor.getColumnIndex(NotesEntry.COLUMN_TITLE));
				String text = cursor.getString(cursor.getColumnIndex(NotesEntry.COLUMN_TEXT));
				long time = cursor.getLong(cursor.getColumnIndex(NotesEntry.COLUMN_TIME));
				Priority priority = Priority.fromInt(cursor.getInt(cursor.getColumnIndex(NotesEntry.COLUMN_PRIORITY)));

				notes.add(new Note(id, title, text, time, priority));
			}

			cursor.close();
		}
		db.close();

		return notes;
	}

}
