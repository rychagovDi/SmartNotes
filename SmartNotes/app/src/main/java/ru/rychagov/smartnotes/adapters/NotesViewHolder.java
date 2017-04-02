package ru.rychagov.smartnotes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.rychagov.smartnotes.R;

class NotesViewHolder extends RecyclerView.ViewHolder {

	private RelativeLayout root;
	private TextView title;
	private TextView time;
	private OnNoteClickListener onNoteClickListener;

	NotesViewHolder(View itemView, NoteCallback callback) {
		super(itemView);

		root = (RelativeLayout) itemView.findViewById(R.id.note_item_root);
		title = (TextView) itemView.findViewById(R.id.note_item_title);
		time = (TextView) itemView.findViewById(R.id.note_item_time);
		onNoteClickListener = new OnNoteClickListener(callback);
	}

	RelativeLayout getRoot() {
		return root;
	}

	TextView getTitle() {
		return title;
	}

	TextView getTime() {
		return time;
	}

	OnNoteClickListener getOnNoteClickListener() {
		return onNoteClickListener;
	}

	class OnNoteClickListener implements View.OnClickListener {

		private NoteCallback callback;
		private int position;

		private OnNoteClickListener(NoteCallback callback) {
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			callback.onNoteClick(position);
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
}
