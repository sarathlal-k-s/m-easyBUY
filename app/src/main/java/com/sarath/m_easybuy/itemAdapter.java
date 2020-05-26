package com.sarath.m_easybuy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.MalformedURLException;
import java.net.URL;

public class itemAdapter extends FirestoreRecyclerAdapter<adModel,itemAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private OnListItemClick onListItemClick;

    public itemAdapter(@NonNull FirestoreRecyclerOptions<adModel> options,OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.aditem,parent,false);
        return new ViewHolder(view,onListItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,adModel model) {
        holder.title.setText(model.getTitle());
        holder.price.setText("₹ "+model.getPrice());
        holder.description.setText(model.getDescription());
        if(model.getImage() != "noimage"){
            Log.d("dd","There is an image");
            Picasso.get().load(model.getImage()).into(holder.image);
        }
        else{
            holder.setIsRecyclable(false);
            Log.d("dd","there is no image");
            Picasso.get().load(model.getImage()).placeholder(R.drawable.imageplaceholder).into(holder.image);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        OnListItemClick listener;
        TextView title,price,description;
        ImageView image;
        public ViewHolder(@NonNull View itemView, final OnListItemClick listener) {
            super(itemView);
            this.listener = listener;
            title = itemView.findViewById(R.id.textViewTitle);
            price = itemView.findViewById(R.id.textViewPrice);
            description = itemView.findViewById(R.id.textViewDescription);
            image = itemView.findViewById(R.id.imageViewImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
        }
    }

    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

}