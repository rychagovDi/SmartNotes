package ru.rychagov.smartnotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;

import static ru.rychagov.smartnotes.database.DataBaseHelper.NotesEntry;

public class DataBaseUtils {

	/**
	 * Обращается к базе данных и получает из нее список заметок
	 */
	public static ArrayList<Note> getNotes(Context context) {

		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

		Cursor cursor = db.query(NotesEntry.TABLE_NAME, null, null, null, null, null, null);

		ArrayList<Note> notes = new ArrayList<>();

		if (cursor != null) {
			cursor.moveToPosition(-1);

			while (cursor.moveToNext()) {
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

	/**
	 * Добавляет заметку Note в базу данных
	 */
	public static void addNote(Context context, Note note) {

		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

		db.insert(NotesEntry.TABLE_NAME, null, getContentValues(note));
		db.close();
	}

	/**
	 * Удаляет заметку Note из базы данных
	 */
	public static void deleteNote(Context context, Note note) {

		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

		db.delete(NotesEntry.TABLE_NAME, NotesEntry._ID + "=?", new String[] {"" + note.getId()});

		db.close();
	}

	/**
	 * Обновляет информацию о заметке Note в базе данных
	 */
	public static void updateNote(Context context, Note note) {

		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

		db.update(NotesEntry.TABLE_NAME, getContentValues(note), NotesEntry._ID, new String[] {"" + note.getId()});

		db.close();
	}

	/**
	 * Возвращает максимальный ID среди всех заметок
	 */
	public static int getMaxID(Context context) {

		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

		int maxID = 0;

		Cursor cursor = db.query(
						NotesEntry.TABLE_NAME,
						new String[] {String.format("MAX(%s)", NotesEntry._ID)},
						null, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
			maxID = cursor.getInt(cursor.getColumnIndex(String.format("MAX(%s)", NotesEntry._ID)));
			cursor.close();
		}

		db.close();

		return maxID;
	}

	/**
	 * Вспомогательный метод, возвращает ContentValues
	 */
	private static ContentValues getContentValues(Note note) {

		ContentValues values = new ContentValues();
		values.put(NotesEntry._ID, note.getId());
		values.put(NotesEntry.COLUMN_TITLE, note.getTitle());
		values.put(NotesEntry.COLUMN_TEXT, note.getText());
		values.put(NotesEntry.COLUMN_TIME, note.getTime());
		values.put(NotesEntry.COLUMN_PRIORITY, note.getPriority().getInt());

		return values;
	}

}
