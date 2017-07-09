package com.stoyanov.developer.goevent.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stoyanov.developer.goevent.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Item> data;
    private Context context;
    private int[] colors = new int[]{R.color.brown, R.color.blue, R.color.red, R.color.cyan,
            R.color.teal, R.color.yellow, R.color.deepOrange, R.color.purple, R.color.pink,
            R.color.deepPurple};

    public CategoryAdapter(@NonNull List<Item> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item i = data.get(position);

        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, colors[position % 10]));
        holder.txtAmount.setText(String.format(Locale.getDefault(), "%d+", i.getAmount()));
        holder.txtName.setText(i.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.slide_page_events_cardview)
        CardView card;

        @BindView(R.id.txt_category_name)
        TextView txtName;

        @BindView(R.id.txt_category_amount)
        TextView txtAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class Item {
        private String name;
        private int amount;

        private Item(Builder builder) {
            setName(builder.name);
            setAmount(builder.amount);
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public static final class Builder {
            private String name;
            private int amount;

            private Builder() {
            }

            public Builder name(String val) {
                name = val;
                return this;
            }

            public Builder amount(int val) {
                amount = val;
                return this;
            }

            public Item build() {
                return new Item(this);
            }
        }
    }
}
