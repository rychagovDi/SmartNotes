package ru.rychagov.smartnotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "smartNote.db";
	private static final int DATABASE_VERSION = 1;

	DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String SQL_CREATE_TABLE = "CREATE TABLE " + NotesEntry.TABLE_NAME + " ("
						+ NotesEntry._ID + " INTEGER NOT NULL, "
						+ NotesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
						+ NotesEntry.COLUMN_TEXT + " TEXT NOT NULL, "
						+ NotesEntry.COLUMN_TIME + " INTEGER NOT NULL, "
						+ NotesEntry.COLUMN_PRIORITY + " INTEGER NOT NULL);";

		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	static class NotesEntry implements BaseColumns {
		static final String TABLE_NAME = "notes_entry";
		static final String _ID = BaseColumns._ID;
		static final String COLUMN_TITLE = "title";
		static final String COLUMN_TEXT = "text";
		static final String COLUMN_TIME = "time";
		static final String COLUMN_PRIORITY = "priority";
	}
}
