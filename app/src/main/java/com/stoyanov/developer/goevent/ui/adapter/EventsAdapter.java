package com.stoyanov.developer.goevent.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.mvp.model.domain.Event;
import com.stoyanov.developer.goevent.mvp.model.domain.Location;
import com.stoyanov.developer.goevent.utill.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> data;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnLikeButtonClickListener onLikeButtonClickListener;

    public EventsAdapter(Context context, OnItemClickListener listener,
                         OnLikeButtonClickListener onLikeButtonClickListener) {
        this.onLikeButtonClickListener = onLikeButtonClickListener;
        onItemClickListener = listener;
        data = new ArrayList<>();
        this.context = context;
    }

    public void removeAndAdd(List<Event> events) {
        data.clear();
        if (events != null) data.addAll(events);
        notifyDataSetChanged();
    }

    public List<Event> getData() {
        return data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_event, parent, false);
        return new ViewHolder(view, onItemClickListener, onLikeButtonClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = data.get(position);
        String urlPicture = event.getPicture();
        if (urlPicture != null) {
            Picasso.with(context)
                    .load(urlPicture)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        }
        holder.when.setText(DateUtil.toDuration(DateUtil.toDate(event.getStartTime()),
                DateUtil.toDate(event.getEndTime())));
        Location location = event.getLocation();
        if (location != null) {
            holder.location.setText(context.getResources()
                    .getString(R.string.location_field_full_format,
                            location.getCity(),
                            location.getCountry(),
                            location.getStreet())
            );

        } else {
            // FIXME: 03.11.2016
        }
        holder.name.setText(event.getName());
    }

    @Override
    public int getItemCount() {
        return (data != null ? data.size() : 0);
    }

    public interface OnItemClickListener {

        void onItem(int position);

    }

    public interface OnLikeButtonClickListener {

        void onLikeClick(int position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView name;
        public TextView when;
        public ImageView image;
        public TextView location;
        private OnItemClickListener itemClickListener;
        private OnLikeButtonClickListener likeButtonClickListener;
        private ImageView like;

        public ViewHolder(View view, OnItemClickListener itemClickListener,
                          OnLikeButtonClickListener likeButtonClickListener) {
            super(view);
            this.itemClickListener = itemClickListener;
            this.likeButtonClickListener = likeButtonClickListener;
            name = (TextView) view.findViewById(R.id.item_event_name);
            when = (TextView) view.findViewById(R.id.item_event_when);
            location = (TextView) view.findViewById(R.id.item_event_where);
            image = (ImageView) view.findViewById(R.id.card_item_image);
            like = (ImageView) view.findViewById(R.id.card_item_like);

            like.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.card_item_image && itemClickListener != null) {
                itemClickListener.onItem(getAdapterPosition());
            } else if (view.getId() == R.id.card_item_like && likeButtonClickListener != null) {
                likeButtonClickListener.onLikeClick(getAdapterPosition());
            }
        }
    }
}
