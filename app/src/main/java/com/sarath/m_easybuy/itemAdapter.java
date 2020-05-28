package com.sarath.m_easybuy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class itemAdapter extends FirestoreRecyclerAdapter<adModel,itemAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private OnListItemClick onListItemClick;
    private int type;

    public itemAdapter(@NonNull FirestoreRecyclerOptions<adModel> options,OnListItemClick onListItemClick,int type) {
        super(options);
        this.onListItemClick = onListItemClick;
        this.type = type;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(type == 2){
            Log.d("dd","2nd type");
            view = layoutInflater.from(parent.getContext()).inflate(R.layout.myadsaditem,parent,false);
        }
        else{
            Log.d("dd","1st type");
            view = layoutInflater.from(parent.getContext()).inflate(R.layout.aditem,parent,false);
        }
        return new ViewHolder(view,onListItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,adModel model) {
        holder.title.setText(model.getTitle());
        holder.price.setText("₹ "+model.getPrice());
        holder.description.setText(model.getDescription());
        holder.username.setText(model.getPublisherName());
        Log.d("dd","getting image");
        if(!model.getImage().equals("noimage")){
            Picasso.get().load(model.getImage()).placeholder(R.drawable.loading).into(holder.image);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        OnListItemClick listener;
        TextView title,price,description,username;
        ImageView image,delete;
        public ViewHolder(@NonNull View itemView, final OnListItemClick listener) {
            super(itemView);
            this.listener = listener;
            title = itemView.findViewById(R.id.textViewTitle);
            price = itemView.findViewById(R.id.textViewPrice);
            description = itemView.findViewById(R.id.textViewDescription);
            image = itemView.findViewById(R.id.imageViewImage);
            username = itemView.findViewById(R.id.textViewUsername);

            if(type == 1) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onItemClick(getSnapshots().getSnapshot(position));
                        }
                    }
                });
            }
            else{
                LinearLayout linearLayout = itemView.findViewById(R.id.adBody);
                delete = itemView.findViewById(R.id.deleteAd);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Log.d("dd","delete pressed at position"+Integer.toString(position));
                        listener.onDeleteClick(getSnapshots().getSnapshot(position));
                    }
                });
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onItemClick(getSnapshots().getSnapshot(position));
                        }
                    }
                });
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onItemClick(getSnapshots().getSnapshot(position));
                        }
                    }
                });
            }
        }
    }


    public interface OnListItemClick{
        void onItemClick(DocumentSnapshot documentSnapshot);
        void onDeleteClick(DocumentSnapshot documentSnapshot);
    }

}