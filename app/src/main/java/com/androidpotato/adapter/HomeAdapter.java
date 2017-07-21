package com.androidpotato.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidpotato.R;
import com.androidpotato.dto.MainPageItem;

import java.util.List;

/**
 * Created by David on 2017/7/19 0019.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private static final String TAG = "HomeAdapter";
    private Context context;
    private List<MainPageItem> items;
    private OnHomeAdapterListener onHomeAdapterListener;

    public HomeAdapter(Context context, List<MainPageItem> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnHomeAdapterListener(OnHomeAdapterListener onHomeAdapterListener) {
        this.onHomeAdapterListener = onHomeAdapterListener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeViewHolder holder = new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_main, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getName());
        if (onHomeAdapterListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onHomeAdapterListener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onHomeAdapterListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public HomeViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.adapter_main_TextView);
        }
    }
    public interface OnHomeAdapterListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
