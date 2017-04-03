package ru.rychagov.smartnotes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
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

	private int position; // Позиция в списке просматриваемой заметки
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

		// Если до пересоздания активность находилась в режиме редактирования, возвращает в него
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
			removeNote();
			return true;
		}

		if (item.getItemId() == R.id.action_edit_note) {
			if (!editing) {
				openEditMode();
			}
			return true;
		}

		if (item.getItemId() == R.id.action_save_as_file) {
			saveAsFile();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		// Если активность находилась в режиме редактирования - переводит в режим просмотра
		if (editing) {
			closeEditMode();
			return;
		}

		super.onBackPressed();
	}

	/**
	 * Обновляет информацию в пользовательском интерфейсе
	 */
	private void updateUI() {
		title.setText(note.getTitle());
		titleRoot.setBackground(getGradient(note.getPriority()));
		text.setText(note.getText());
		time.setText(NoteUtils.getStringDate(note.getTime()));
	}

	/**
	 * Сохраняет заметку в виде текстового файла.
	 * Если пользователь не выдал разрешение на запись - выводит сообщение об этом
	 */
	private void saveAsFile() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

			String path = NoteUtils.saveAsFile(note);

			if (path != null) {
				Toast.makeText(getApplicationContext(), getString(R.string.saved_in) + " " + path, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), R.string.saving_error, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.save_as_file_permission, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Закрывает текущую активность и передает информацию в вызывающую активность, что заметку на позиции
	 * position небходимо удалить
	 */
	private void removeNote() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_POSITION, position);
		setResult(RESULT_REMOVE, intent);
		finish();
	}

	/**
	 * Переводит активность в режим редактирования заметки
	 */
	private void openEditMode() {
		editTitle.setText(note.getTitle());
		editText.setText(note.getText());
		spinner.setSelection(note.getPriority().getInt());

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_edit_mode_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_edit_mode_out));
		viewFlipper.showNext();
		editing = true;
	}

	/**
	 * Переводит активность в режим просмотра заметки
	 */
	private void closeEditMode() {
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_edit_mode_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_edit_mode_out));
		viewFlipper.showPrevious();
		editing = false;
	}

	public void close(View view) {
		closeEditMode();
	}

	public void apply(View view) {
		applyChanges();
	}

	/**
	 * Сохраняет изменения, внесенные в заметку
	 */
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

	/**
	 * Проверяет правильность заполнения полей в режиме редактирования заметки
	 */
	private boolean isValid() {
		return (!editText.getText().toString().trim().equals("") && !editTitle.getText().toString().trim().equals(""));
	}

	/**
	 * Вовзращает объект {@link GradientDrawable}, соответствующий определенной важности заметки
	 */
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
