package ru.rychagov.smartnotes.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.rychagov.smartnotes.NoteUtils;
import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;
import ru.rychagov.smartnotes.database.DataBaseUtils;

public class PreviewActivity extends AppCompatActivity {

	public static final String EXTRA_POSITION = "EXTRA_POSITION";
	public static final String EXTRA_ID = "EXTRA_ID";

	private int position;
	private Note note;

	private RelativeLayout titleRoot;
	private TextView title;
	private TextView text;
	private TextView time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_preview);

		setTitle(R.string.preview_title);

		titleRoot = (RelativeLayout) findViewById(R.id.preview_title_root);
		title = (TextView) findViewById(R.id.preview_title);
		text = (TextView) findViewById(R.id.preview_text);
		time = (TextView) findViewById(R.id.preview_time);

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

	private GradientDrawable getGradient (Priority priority) {

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
				drawable = new GradientDrawable();
				break;
		}

		return drawable;
	}
}