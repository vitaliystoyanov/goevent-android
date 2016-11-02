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

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public EventsAdapter(Context context, OnItemClickListener listener) {
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
        return new ViewHolder(view, onItemClickListener);
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
        holder.when.setText(event.getStartTime());
        Location location = event.getLocation();
        if (location != null) {
            holder.where.setText(location.getStreet());
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public OnItemClickListener listener;
        public TextView name;
        public TextView when;
        public ImageView image;
        public TextView where;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.listener = listener;
            name = (TextView) view.findViewById(R.id.item_event_name);
            when = (TextView) view.findViewById(R.id.item_event_when);
            where = (TextView) view.findViewById(R.id.item_event_where);
            image = (ImageView) view.findViewById(R.id.card_item_image);

//            view.findViewById(R.id.card_item_more_button).setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) listener.onItem(getAdapterPosition());
        }
    }
}
