package com.stoyanov.developer.goevent.ui.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.GoeventApplication;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.manager.FavoriteManager;
import com.stoyanov.developer.goevent.utill.DateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    @Inject
    FavoriteManager favoriteManager;
    private List<Event> data;
    private Context context;
    private OnLikeItemClickListener onLikeItemClickListener;
    private OnItemClickListener onItemClickListener;

    public EventsAdapter(Context context, OnItemClickListener onItemClickListener,
                         OnLikeItemClickListener onLikeItemClickListener) {
        (GoeventApplication.getApplicationComponent(context)).inject(this);
        this.onLikeItemClickListener = onLikeItemClickListener;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        data = new ArrayList<>();
    }

    public void removeAndAdd(List<Event> events) {
        data.clear();
        data.addAll(events);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    public void removeAll() {
        data.clear();
        notifyDataSetChanged();
    }

    public Event getItem(int position) {
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_event, parent, false);
        return new ViewHolder(view, onItemClickListener, onLikeItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Event event = data.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);
        String urlPicture = event.getPicture();
            Picasso.with(context)
                    .load(urlPicture)
                    .fit()
                    .centerCrop()
                    .into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            holder.image.setImageResource(R.drawable.background_splash);
                        }
                    });
        if (event.getStartTime() != null && event.getEndTime() != null) {
            holder.when.setText(DateUtil.toDuration(DateUtil.toDate(event.getStartTime()),
                    DateUtil.toDate(event.getEndTime())));
        }
        if (event.getCategory() != null) {
            holder.category.setText(event.getCategory());
        } else {
            holder.category.setText(R.string.field_no_category);
        }
        Location location = event.getLocation();
        if (location != null) {
            holder.location.setText(
                    context.getResources().getString(R.string.location_field_full_format,
                            location.getCity(),
                            location.getCountry(),
                            location.getStreet()));
        } else {
            holder.location.setText(R.string.field_no_location);
        }
        holder.star.setLiked(favoriteManager.isSaved(event));
        holder.name.setText(event.getName());
    }

    @Override
    public int getItemCount() {
        return (data != null ? data.size() : 0);
    }

    public List<Event> getItems() {
        return data;
    }

    public interface OnItemClickListener {
        void onItem(int position);
    }

    public interface OnLikeItemClickListener extends OnLikeListener {
        void onItem(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView name;
        public TextView when;
        public ImageView image;
        public TextView location;
        public TextView category;
        public ProgressBar progressBar;
        private LikeButton star;
        private OnItemClickListener itemClickListener;
        private OnLikeItemClickListener onLikeItemClickListener;

        public ViewHolder(View view, OnItemClickListener itemClickListener,
                          OnLikeItemClickListener onLikeItemClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.onLikeItemClickListener = onLikeItemClickListener;
            name = (TextView) view.findViewById(R.id.item_event_name);
            when = (TextView) view.findViewById(R.id.item_event_when);
            category = (TextView) view.findViewById(R.id.item_event_category);
            location = (TextView) view.findViewById(R.id.item_event_where);
            image = (ImageView) view.findViewById(R.id.card_item_image);
            star = (LikeButton) view.findViewById(R.id.card_item_star);
            progressBar = (ProgressBar) view.findViewById(R.id.card_item_progress_bar);
            star.setIconSizeDp(22);
            star.setIcon(IconType.Star); // FIXME: 22.11.2016 to xml

            star.setOnLikeListener(onLikeItemClickListener);
            star.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.card_item_image && itemClickListener != null) {
                itemClickListener.onItem(getAdapterPosition());
            } else if (view.getId() == R.id.card_item_star && onLikeItemClickListener != null) {
                onLikeItemClickListener.onItem(getAdapterPosition());
                star.onClick(view);
            }
        }
    }
}
