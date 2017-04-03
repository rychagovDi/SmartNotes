package ru.rychagov.smartnotes.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class AntiBugLinearLayoutManager extends LinearLayoutManager {

	public AntiBugLinearLayoutManager(Context context) {
		super(context);
	}

	// Ошибка внутри Андроида, которую все никак не могут исправить
	// Приходится использовать такой костыль
	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
		try {
			super.onLayoutChildren(recycler, state);
		} catch (IndexOutOfBoundsException e) {
			Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
		}
	}
}
