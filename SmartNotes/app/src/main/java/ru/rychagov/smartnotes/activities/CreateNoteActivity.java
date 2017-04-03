package ru.rychagov.smartnotes.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.SpinnerAdapter;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class CreateNoteActivity extends AppCompatActivity {

	public static final int RESULT_CANCEL = 1;
	public static final int RESULT_ADD = 2;

	private Spinner spinner;
	private EditText titleEdit;
	private EditText textEdit;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_create_note);

		setTitle(R.string.add_note);

		context = getApplicationContext();

		titleEdit = (EditText) findViewById(R.id.create_note_title);
		textEdit = (EditText) findViewById(R.id.create_note_text);

		spinner = (Spinner) findViewById(R.id.create_note_spinner);
		spinner.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item));
	}

	/**
	 * Закрывает текущую активность и сообщает вызывающей активности, что заметка не была создана
	 */
	public void cancel(View view) {
		setResult(RESULT_CANCEL);
		finish();
	}

	/**
	 * Звкрывает текущую активность, добавляет заметку в базу данных и сообщает вызывающей активности об этом
	 */
	public void add(View view) {

		if (!isValid()) {
			Toast.makeText(this, R.string.create_note_toast, Toast.LENGTH_SHORT).show();
			return;
		}

		setResult(RESULT_ADD);

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

		int id = DataBaseUtils.getMaxID(context) + 1;
		String title = titleEdit.getText().toString().trim();
		String text = textEdit.getText().toString().trim();
		long time = System.currentTimeMillis();
		Priority priority = Priority.fromInt(spinner.getSelectedItemPosition());

		Note note = new Note(id, title, text, time, priority);

		DataBaseUtils.addNote(context, note);

		finish();
	}

	/**
	 * Проверяет правильность заполнения полей
	 */
	private boolean isValid() {
		return (!textEdit.getText().toString().trim().equals("") && !titleEdit.getText().toString().trim().equals(""));
	}

}
