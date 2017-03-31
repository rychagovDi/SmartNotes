package ru.rychagov.smartnotes.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.rychagov.smartnotes.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

	private Context context;

	public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, String[] objects) {
		super(context, resource, objects);
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}


	private View getCustomView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
		}

		TextView text = (TextView) convertView.findViewById(R.id.spinner_text_view);

		switch (position) {
			case 0:
				text.setBackgroundResource(R.color.priority_default);
				text.setText(R.string.priority_default);
				break;
			case 1:
				text.setBackgroundResource(R.color.priority_low);
				text.setText(R.string.priority_low);
				break;
			case 2:
				text.setBackgroundResource(R.color.priority_medium);
				text.setText(R.string.priority_medium);
				break;
			case 3:
				text.setBackgroundResource(R.color.priority_high);
				text.setText(R.string.priority_high);
				break;
		}
		return convertView;
	}
}
