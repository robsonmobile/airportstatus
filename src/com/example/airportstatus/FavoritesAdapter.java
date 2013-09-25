package com.example.airportstatus;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.example.airportstatus.models.Favorite;

public class FavoritesAdapter extends ArrayAdapter<Favorite> {
	public FavoritesAdapter(Context context, List<Favorite> favorites) {
		super(context, android.R.layout.simple_spinner_item, android.R.id.text1, favorites);
	}
}
