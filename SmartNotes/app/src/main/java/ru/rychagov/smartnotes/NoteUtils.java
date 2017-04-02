package ru.rychagov.smartnotes;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;

public class NoteUtils {

	/**
	 * Возвращает цвет, соответствующий определенной важности заметки
	 */
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

	/**
	 * Преобразует timestamp в строковое представление даты
	 */
	public static String getStringDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);
		return sdf.format(new Date(time));
	}

	/**
	 * Сохраняет заметку в виде файла.
	 * Возвращает директорию, в которой сохранился файл, либо null при ошибке сохранения
	 */
	public static String saveAsFile(Note note) {
		File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");

		String path = docsFolder.getPath();

		if (!docsFolder.exists()) {
			docsFolder.mkdir();
		}

		File file = new File(path, String.format("%s.txt", note.getTime()));

		try {
			FileWriter writer = new FileWriter(file);
			writer.append(String.format("Заметка: %s \nСоздана %s\n\n%s", note.getTitle(), getStringDate(note.getTime()), note.getText()));

			Log.d("NoteUtils", file.getAbsolutePath());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			path = null;
			e.printStackTrace();
		}

		return path;
	}
}
