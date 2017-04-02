package ru.rychagov.smartnotes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.rychagov.smartnotes.R;
import ru.rychagov.smartnotes.NoteUtils;
import ru.rychagov.smartnotes.data.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

	private ArrayList<Note> notes;
	private NoteCallback callback;

	public NotesAdapter(ArrayList<Note> notes, NoteCallback callback) {
		this.notes = notes;
		this.callback = callback;
	}

	@Override
	public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);

		NotesViewHolder viewHolder = new NotesViewHolder(view, callback);
		view.setOnClickListener(viewHolder.getOnNoteClickListener());

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(NotesViewHolder holder, int position) {
		Note note = notes.get(position);

		holder.getRoot().setBackgroundResource(NoteUtils.getColorFromPriority(note.getPriority()));
		holder.getTitle().setText(note.getTitle());
		holder.getTime().setText(NoteUtils.getStringDate(note.getTime()));
		holder.getOnNoteClickListener().setPosition(position);
	}

	@Override
	public int getItemCount() {
		return notes.size();
	}
}
