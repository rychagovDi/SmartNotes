package ru.rychagov.smartnotes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.data.Note;
import ru.rychagov.smartnotes.data.Priority;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

	private ArrayList<Note> notes;

	public NotesAdapter(ArrayList<Note> notes) {
		this.notes = notes;
	}

	@Override
	public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
		return new NotesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(NotesViewHolder holder, int position) {
		Note note = notes.get(position);

		holder.getRoot().setBackgroundResource(getColorFromPriority(note.getPriority()));
		holder.getTitle().setText(note.getTitle());
		// TODO написать метод по преобразованию времени в строку
		holder.getTime().setText("" + note.getTime());
	}

	@Override
	public int getItemCount() {
		return notes.size();
	}

	private int getColorFromPriority(Priority priority) {

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
}
