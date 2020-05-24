package com.sarath.m_easybuy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class itemAdapter extends FirestoreRecyclerAdapter<adModel,itemAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;

    public itemAdapter(@NonNull FirestoreRecyclerOptions<adModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.aditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,adModel model) {
        holder.title.setText(model.getTitle());
        holder.price.setText("₹"+model.getPrice());
        holder.description.setText(model.getDescription());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,price,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            price = itemView.findViewById(R.id.textViewPrice);
            description = itemView.findViewById(R.id.textViewDescription);
        }
    }
}