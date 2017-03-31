package ru.rychagov.smartnotes.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.SpinnerAdapter;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class CreateNoteActivity extends AppCompatActivity {

	public static final int RESULT_CANCEL = 1;
	public static final int RESULT_ADD = 2;

	private Spinner spinner;
	private EditText title;
	private EditText text;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_create_note);

		setTitle(R.string.add_note);

		context = getApplicationContext();

		title = (EditText) findViewById(R.id.create_note_title);
		text = (EditText) findViewById(R.id.create_note_text);

		spinner = (Spinner) findViewById(R.id.create_note_spinner);
		spinner.setAdapter(new SpinnerAdapter(getApplicationContext(),
						android.R.layout.simple_spinner_item,
						new String[] {"Без приоритета", "Низкий", "Средний", "Высокий"}));
	}

	public void cancel(View view) {
		setResult(RESULT_CANCEL);
		finish();
	}

	public void add(View view) {
		setResult(RESULT_ADD);
	}

}
