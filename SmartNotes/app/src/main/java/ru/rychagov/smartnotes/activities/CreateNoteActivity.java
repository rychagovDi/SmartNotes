package ru.rychagov.smartnotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.adapters.SpinnerAdapter;

public class CreateNoteActivity extends AppCompatActivity {

	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_create_note);

		setTitle(R.string.add_note);

		spinner = (Spinner) findViewById(R.id.create_note_spinner);
		spinner.setAdapter(new SpinnerAdapter(getApplicationContext(),
						android.R.layout.simple_spinner_item,
						new String[] {"Без приоритета", "Низкий", "Средний", "Высокий"}));
	}

}
