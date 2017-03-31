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

	NotesViewHolder(View itemView) {
		super(itemView);

		root = (RelativeLayout) itemView.findViewById(R.id.note_item_root);
		title = (TextView) itemView.findViewById(R.id.note_item_title);
		time = (TextView) itemView.findViewById(R.id.note_item_time);
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
}
