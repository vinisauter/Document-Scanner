package com.nguyenhoanglam.imagepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.R;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final OnItemClickListener onItemClickListener;
    private OnCountChanged onCountChanged;
    private Context context;
    private LayoutInflater inflater;
    private final List<Image> selectedImages;

    public ImageAdapter(Context context, List<Image> selectedImages, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.selectedImages = new ArrayList<>(selectedImages);
        if (this.selectedImages.size() > 0)
            lastCount = selectedImages.size();
        inflater = LayoutInflater.from(this.context);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View itemView = inflater.inflate(R.layout.item_selected_image, parent, false);
            return new FooterViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = inflater.inflate(R.layout.item_selected_image, parent, false);
            return new ImageViewHolder(itemView, onItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {
        if (viewHolder instanceof FooterViewHolder) {
            viewHolder.bind(context, null);
        } else {
            Image image = selectedImages.get(position);
            viewHolder.bind(context, image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == selectedImages.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private int lastCount = -1;

    @Override
    public int getItemCount() {
        return selectedImages.size() + 1;
    }

    public void setOnCountChanged(OnCountChanged onCountChanged) {
        this.onCountChanged = onCountChanged;
    }

    public void setData(List<Image> images) {
        this.selectedImages.clear();
        this.selectedImages.addAll(images);
        onCountChanged();
    }

    public void addAll(List<Image> images) {
        int startIndex = this.selectedImages.size();
        this.selectedImages.addAll(startIndex, images);
        notifyItemRangeInserted(startIndex, images.size());
        onCountChanged();
    }

    public void addSelected(Image image) {
        selectedImages.add(image);
        notifyItemChanged(selectedImages.indexOf(image));
        onCountChanged();
    }

    public void removeSelectedImage(Image image) {
        int position = selectedImages.indexOf(image);
        selectedImages.remove(image);
        notifyItemRemoved(position);
        onCountChanged();
    }

    public void removeSelectedImage(int position) {
        selectedImages.remove(position);
        notifyItemRemoved(position);
        onCountChanged();
    }

    public void removeAllSelectedSingleClick() {
        selectedImages.clear();
        notifyDataSetChanged();
        onCountChanged();
    }

    private void onCountChanged() {
        boolean countChanged = lastCount != selectedImages.size();
        if (countChanged) {
            onCountChanged.onCountChanged(lastCount, selectedImages.size());
            lastCount = selectedImages.size();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Image image);
    }

    public interface OnCountChanged {
        void onCountChanged(int lastCount, int size);
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        Image bImage;
        final OnItemClickListener onItemClickListener;
        ImageView imageView;

        ImageViewHolder(final View itemView) {
            this(itemView, null);
        }

        ImageViewHolder(final View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            this.onItemClickListener = onItemClickListener;
        }


        void bind(Context context, Image image) {
            itemView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(image.getPath())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(imageView);
            this.bImage = image;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(itemView, position, bImage);
                }
            });
        }
    }

    private class FooterViewHolder extends ImageViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(Context context, Image image) {
            itemView.setVisibility(View.INVISIBLE);
        }
    }

}

