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

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> data;
    private Context context;

    public EventsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Event> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_event, parent, false);
        return new ViewHolder(view);
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
        holder.where.setText("Location not define");
        holder.name.setText(event.getName());
    }

    @Override
    public int getItemCount() {
        return (data != null ? data.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView when;
        public TextView where;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.item_event_name);
            this.when = (TextView) view.findViewById(R.id.item_event_when);
            this.where = (TextView) view.findViewById(R.id.item_event_where);
            this.image = (ImageView) view.findViewById(R.id.card_item_image);
        }
    }
}
