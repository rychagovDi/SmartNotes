package ru.rychagov.smartnotes.activities;

import android.app.Activity;
import android.os.Bundle;

import ru.rychagov.smartnotes.R;

public class NotesListActivity extends Activity {

	private static final String TAG = "NotesListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_notes_list);
	}
}
