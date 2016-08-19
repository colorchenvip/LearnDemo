package com.colorchen.mvp.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colorchen.R;
import com.colorchen.lib.ArrayRecyclerAdapter;
import com.colorchen.lib.RatioImageView;
import com.colorchen.mvp.model.Image;
import com.colorchen.utils.Imager;

/**
 * Created by color on 16/4/26 16:12.
 */
public abstract class PictureAdapter extends ArrayRecyclerAdapter<Image, PictureAdapter.ViewHolder> {


    private Context context;

    public PictureAdapter(Context context) {
        this.context = context;
    }

    protected abstract void onItemClick(View v, int position);


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = get(position);
        holder.imageView.setOriginalSize(image.getWidth(), image.getHeight());
        Imager.load(holder.itemView.getContext(), image.getUrl(), holder.imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RatioImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (RatioImageView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
