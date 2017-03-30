package ru.rychagov.smartnotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import ru.rychagov.smartnotes.R;

public class NotesListActivity extends AppCompatActivity {

	private static final String TAG = "NotesListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_notes_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.notes_list_menu, menu);
		return true;
	}
}
