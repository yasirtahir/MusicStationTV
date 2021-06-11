package com.yasir.musicstation.tv.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.yasir.musicstation.tv.R;
import com.yasir.musicstation.tv.activities.PlayerActivity;
import com.yasir.musicstation.tv.models.CategoryModel;
import com.yasir.musicstation.tv.models.Song;
import com.yasir.musicstation.tv.presenter.CardPresenter;
import com.yasir.musicstation.tv.utils.DataUtil;

@SuppressWarnings("ALL")
public class MainFragment extends BrowseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareBackgroundManager();
        setupUIElements();
        loadRows();
        setupEventListeners();
    }

    private void loadRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        int i;

        for (i = 0; i < DataUtil.getData(getActivity()).size(); i++) {
            CategoryModel categoryModel = DataUtil.getData(getActivity()).get(i);
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < categoryModel.getCategorySongs().size(); j++) {
                listRowAdapter.add(categoryModel.getCategorySongs().get(j));
            }
            HeaderItem header = new HeaderItem(i, categoryModel.getCategoryName());
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {
        BackgroundManager mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(getResources().getColor(R.color.main_background));
        DisplayMetrics mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Song) {
                Song song = (Song) item;
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(DataUtil.SONG_DETAIL, song);
                getActivity().startActivity(intent);
            }
        }
    }
}