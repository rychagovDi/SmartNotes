package ru.rychagov.smartnotes.activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import ru.rychagov.smartnotes.NoteUtils;
import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.SpinnerAdapter;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class PreviewActivity extends AppCompatActivity {

	public static final String EXTRA_POSITION = "EXTRA_POSITION";
	public static final String EXTRA_ID = "EXTRA_ID";

	public static final int RESULT_REMOVE = 1;
	public static final int RESULT_CHANGE = 2;

	private String EDITING = "EDITING";
	private boolean editing;

	private int position;
	private Note note;

	private ViewFlipper viewFlipper;

	private RelativeLayout titleRoot;
	private TextView title;
	private TextView text;
	private TextView time;

	private EditText editTitle;
	private EditText editText;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_preview_note);

		setTitle(R.string.preview_title);

		editing = savedInstanceState != null && savedInstanceState.getBoolean(EDITING);

		viewFlipper = (ViewFlipper) findViewById(R.id.preview_view_flipper);

		if (editing) {
			viewFlipper.showNext();
		}

		titleRoot = (RelativeLayout) viewFlipper.findViewById(R.id.preview_title_root);
		title = (TextView) viewFlipper.findViewById(R.id.preview_title);
		text = (TextView) viewFlipper.findViewById(R.id.preview_text);
		time = (TextView) viewFlipper.findViewById(R.id.preview_time);

		editTitle = (EditText) viewFlipper.findViewById(R.id.preview_edit_title);
		editText = (EditText) viewFlipper.findViewById(R.id.preview_edit_text);
		spinner = (Spinner) viewFlipper.findViewById(R.id.preview_edit_spinner);
		spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item));

		position = getIntent().getIntExtra(EXTRA_POSITION, 0);
		note = DataBaseUtils.getNote(getApplicationContext(), getIntent().getIntExtra(EXTRA_ID, 1));

		updateUI();
	}

	private void updateUI() {
		title.setText(note.getTitle());
		titleRoot.setBackground(getGradient(note.getPriority()));
		text.setText(note.getText());
		time.setText(NoteUtils.getStringDate(note.getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preview_menu, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(EDITING, editing);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_remove_note) {
			Intent intent = new Intent();
			intent.putExtra(EXTRA_POSITION, position);
			setResult(RESULT_REMOVE, intent);
			finish();
			return true;
		}

		if (item.getItemId() == R.id.action_edit_note) {
			if (!editing) {
				openEditMode();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		if (editing) {
			closeEditMode();
			return;
		}

		super.onBackPressed();
	}

	private void openEditMode() {
		editTitle.setText(note.getTitle());
		editText.setText(note.getText());
		spinner.setSelection(note.getPriority().getInt());

		viewFlipper.showNext();
		editing = true;
	}

	private void closeEditMode() {
		viewFlipper.showPrevious();
		editing = false;
	}

	public void close(View view) {
		closeEditMode();
	}

	public void apply(View view) {
		applyChanges();
	}

	private void applyChanges() {

		if (!isValid()) {
			Toast.makeText(this, R.string.create_note_toast, Toast.LENGTH_SHORT).show();
			return;
		}

		setResult(RESULT_CHANGE);

		String title = editTitle.getText().toString().trim();
		String text = editText.getText().toString().trim();
		Priority priority = Priority.fromInt(spinner.getSelectedItemPosition());

		note = new Note(note.getId(), title, text, note.getTime(), priority);

		DataBaseUtils.updateNote(getApplicationContext(), note);

		closeEditMode();

		updateUI();
	}

	private boolean isValid() {
		return (!editText.getText().toString().trim().equals("") && !editTitle.getText().toString().trim().equals(""));
	}

	private GradientDrawable getGradient(Priority priority) {

		GradientDrawable drawable;

		switch (priority) {
			case LOW:
				drawable = (GradientDrawable) getResources().getDrawable(R.drawable.gradient_preview_priority_low);
				break;
			case MEDIUM:
				drawable = (GradientDrawable) getResources().getDrawable(R.drawable.gradient_preview_priority_medium);
				break;
			case HIGH:
				drawable = (GradientDrawable) getResources().getDrawable(R.drawable.gradient_preview_priority_high);
				break;
			case NONE:
			default:
				drawable = (GradientDrawable) getResources().getDrawable(R.drawable.gradient_preview_priority_default);
				break;
		}

		return drawable;
	}
}
