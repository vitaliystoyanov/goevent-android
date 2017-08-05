package com.stoyanov.developer.goevent.ui.events;

import android.content.Context;
import android.support.v13.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>
        implements Filterable {
    @Inject
    FavoriteManager favoriteManager;
    private List<Event> data;
    private Context context;
    private OnLikeItemClickListener onLikeItemClickListener;
    private OnItemClickListener onItemClickListener;
    private CategoryFilter categoryFilter;

    public EventsAdapter(Context context, OnItemClickListener itemClickListener,
                         OnLikeItemClickListener likeItemClickListener) {
        (GoeventApplication.getApplicationComponent(context)).inject(this);
        this.onLikeItemClickListener = likeItemClickListener;
        this.onItemClickListener = itemClickListener;
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
        ViewCompat.setTransitionName(holder.image, event.getName());
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

    @Override
    public Filter getFilter() {
        if (categoryFilter == null) categoryFilter = new CategoryFilter(this, new ArrayList<>(data));
        return categoryFilter;
    }

    public interface OnItemClickListener {
        void onItem(int position, ImageView sharedImageView, String transitionName);
    }

    public interface OnLikeItemClickListener extends OnLikeListener {
        void onItem(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.item_event_name)
        TextView name;
        @BindView(R.id.item_event_when)
        TextView when;
        @BindView(R.id.card_item_image)
        ImageView image;
        @BindView(R.id.item_event_where)
        TextView location;
        @BindView(R.id.item_event_category)
        TextView category;
        @BindView(R.id.card_item_progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.card_item_star)
        LikeButton star;
        private OnItemClickListener itemClickListener;
        private OnLikeItemClickListener onLikeItemClickListener;

        public ViewHolder(View view, OnItemClickListener itemClickListener,
                          OnLikeItemClickListener onLikeItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.itemClickListener = itemClickListener;
            this.onLikeItemClickListener = onLikeItemClickListener;

            star.setIconSizeDp(22);
            star.setIcon(IconType.Star);
            star.setOnLikeListener(onLikeItemClickListener);
            star.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.card_item_image && itemClickListener != null) {
                itemClickListener.onItem(getAdapterPosition(), image, ViewCompat.getTransitionName(image));
            } else if (view.getId() == R.id.card_item_star && onLikeItemClickListener != null) {
                onLikeItemClickListener.onItem(getAdapterPosition());
                star.onClick(view);
            }
        }
    }

    private static class CategoryFilter extends Filter {
        private final EventsAdapter adapter;
        private final List<Event> originalList;
        private final List<Event> filteredList;

        public CategoryFilter(EventsAdapter adapter, List<Event> originalList) {
            this.adapter = adapter;
            this.originalList = originalList;
            filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            String[] filterCategory = constraint.toString().split(",");
            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                for (String filter : filterCategory) {
                    for (final Event event : originalList) {
                        if (event.getCategory().equals(filter)) {
                            filteredList.add(event);
                        }
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            adapter.data.clear();
            adapter.data.addAll((ArrayList<Event>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
