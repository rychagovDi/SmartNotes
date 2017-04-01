package ru.rychagov.smartnotes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.rychagov.smartnotes.data.Priority;

public class NoteUtils {

	public static int getColorFromPriority(Priority priority) {

		int colorID = R.color.priority_default;

		switch (priority) {

			case LOW:
				colorID = R.color.priority_low;
				break;
			case MEDIUM:
				colorID = R.color.priority_medium;
				break;
			case HIGH:
				colorID = R.color.priority_high;
				break;
		}

		return colorID;
	}

	public static String getStringDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);
		return sdf.format(new Date(time));
	}
}
