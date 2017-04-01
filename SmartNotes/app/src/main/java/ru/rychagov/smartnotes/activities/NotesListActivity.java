package ru.rychagov.smartnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.NotesAdapter;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class NotesListActivity extends AppCompatActivity {

	private static final String TAG = "NotesListActivity";

	private static final int REQUEST_CREATE_NOTE = 1;

	private RecyclerView recyclerView;
	private TextView placeholder;
	private Context context;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CREATE_NOTE) {
			if (resultCode == CreateNoteActivity.RESULT_ADD) {
				updateUI();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_notes_list);

		context = getApplicationContext();

		recyclerView = (RecyclerView) findViewById(R.id.notes_list_recycler);
		placeholder = (TextView) findViewById(R.id.notes_list_placeholder);

		updateUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.notes_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_add_note) {
			Log.d(TAG, "Open CreateNoteActivity");
			startActivityForResult(new Intent(this, CreateNoteActivity.class), REQUEST_CREATE_NOTE);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void updateUI() {
		ArrayList<Note> notes = DataBaseUtils.getNotes(context);

		if (notes.size() != 0) {
			placeholder.setVisibility(View.INVISIBLE); // TODO Проверить, пропадает ли автоматически при повороте экрана
			recyclerView.setLayoutManager(new LinearLayoutManager(context));
			recyclerView.setAdapter(new NotesAdapter(notes));
		} else {
			placeholder.setVisibility(View.VISIBLE);
		}
	}
}
