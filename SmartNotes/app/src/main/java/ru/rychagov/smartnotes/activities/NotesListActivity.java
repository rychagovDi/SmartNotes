package ru.rychagov.smartnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.NoteCallback;
import ru.rychagov.smartnotes.adapters.NotesAdapter;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class NotesListActivity extends AppCompatActivity implements NoteCallback {

	private static final String TAG = "NotesListActivity";

	private static final int REQUEST_CREATE_NOTE = 1;
	private static final int REQUEST_OPEN_NOTE = 2;

	private ArrayList<Note> notes;
	private RecyclerView recyclerView;
	private TextView placeholder;
	private Context context;

	private Note removedNote;
	private int removedNotePosition;
	private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
		@Override
		public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
			return false;
		}

		@Override
		public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
			removeNote(viewHolder.getAdapterPosition());
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

		if (requestCode == REQUEST_CREATE_NOTE) {
			if (resultCode == CreateNoteActivity.RESULT_ADD) {

				updateNotes();

				// Необходимо для более плавной анимации добавления заметки
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// Без этой проверки пролистывание в начало списка выдает ошибку
						if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 0 ||
										((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() < notes.size()) {
							recyclerView.smoothScrollToPosition(0);
						}
						recyclerView.getAdapter().notifyItemInserted(0);

					}
				}, 350);

			}
		}

		if (requestCode == REQUEST_OPEN_NOTE) {
			if (resultCode == PreviewActivity.RESULT_REMOVE) {

				// Необходимо для более плавной анимации удаления заметки
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						removeNote(data.getIntExtra(PreviewActivity.EXTRA_POSITION, 0));
					}
				}, 350);
			}

			if (resultCode == PreviewActivity.RESULT_CHANGE) {
				updateNotes();
				recyclerView.getAdapter().notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_notes_list);

		context = getApplicationContext();

		recyclerView = (RecyclerView) findViewById(R.id.notes_list_recycler);
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
		itemTouchHelper.attachToRecyclerView(recyclerView);
		placeholder = (TextView) findViewById(R.id.notes_list_placeholder);

		initUI();
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

	private void initUI() {
		notes = DataBaseUtils.getNotes(context);

		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		recyclerView.setAdapter(new NotesAdapter(notes, this));

		validatePlaceholder();
	}

	/**
	 * Делает placeholder видимым или невидимым в зависимости от наличия заметок в списке
	 */
	private void validatePlaceholder() {
		if (notes.size() == 0) {
			placeholder.setVisibility(View.VISIBLE);
		} else {
			placeholder.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Загружает новый список заметок из базы данных
	 */
	private void updateNotes() {
		notes.clear();
		notes.addAll(DataBaseUtils.getNotes(context));
		validatePlaceholder();
	}

	/**
	 * Удаляет заметку из списка и базы данных
	 */
	private void removeNote(int position) {
		removedNotePosition = position;
		removedNote = notes.get(removedNotePosition);

		DataBaseUtils.removeNote(context, removedNote);
		notes.remove(removedNotePosition);
		recyclerView.getAdapter().notifyItemRemoved(removedNotePosition);

		showSnackbar();

		validatePlaceholder();
	}

	/**
	 * Выводит Snackbar с сообщением об удалении заметки и кнопкой для отмены удаления
	 */
	private void showSnackbar() {
		Snackbar.make(recyclerView, R.string.item_deleted, Snackbar.LENGTH_SHORT).
						setAction(R.string.button_cancel, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								DataBaseUtils.addNote(context, removedNote);
								notes.add(removedNotePosition, removedNote);
								recyclerView.getAdapter().notifyItemInserted(removedNotePosition);
								placeholder.setVisibility(View.INVISIBLE);
							}
						}).show();
	}

	@Override
	public void onNoteClick(int position) {
		Intent intent = new Intent(this, PreviewActivity.class);
		intent.putExtra(PreviewActivity.EXTRA_ID, notes.get(position).getId());
		intent.putExtra(PreviewActivity.EXTRA_POSITION, position);

		startActivityForResult(intent, REQUEST_OPEN_NOTE);
	}
}
