package com.example.encuentrahipotenocha;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ExampleViewHolder> {

    private ArrayList<Item> mExampleList;
    private OnItemClickListener mListener;
    private OnLongClickListener mLongClickListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
    public interface OnLongClickListener {
        void onLongClick (int position);
    }
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener onLongClickListener){
       this.mLongClickListener=onLongClickListener;
    }

    public Adaptador(ArrayList<Item> exampleList) {
        mExampleList = exampleList;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);    //Usamos el método inflate() para crear una vista a partir del layout XML definido en layout_listitem.
        ExampleViewHolder exampleViewHolder=new ExampleViewHolder(v, mListener, mLongClickListener);

        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ExampleViewHolder holder, int position) {
        Item nuevaImagen=mExampleList.get(position);
        holder.mImageView.setImageResource(nuevaImagen.getImageResource());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener, final OnLongClickListener mLongClickListener) {

            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mLongClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mLongClickListener.onLongClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}
