package com.yasir.musicstation.tv.presenter;

import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yasir.musicstation.tv.R;
import com.yasir.musicstation.tv.models.Song;

public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.background);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.colorPrimaryDark);

        ImageCardView cardView =
                new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Song song = (Song) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setTitleText(song.getSongName());
        cardView.setContentText(song.getArtistName());
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        Glide.with(viewHolder.view.getContext())
                .load(song.getImageURL())
                .centerCrop()
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(cardView.getMainImageView());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}