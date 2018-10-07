package com.example.lab2.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lab2.R;
import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.RecViewHolder> {

    private ArrayList<DataModel> alist;
    public PersonAdapter(ArrayList<DataModel> alist) {
        this.alist = alist;
    }


    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.relative_item, null);
        RecViewHolder viewHolder = new RecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecViewHolder holder, int position) {
        DataModel dataModel = alist.get(position);
        holder.itemName.setText(dataModel.getName());
        holder.itemSurname.setText(dataModel.getSurname());
        holder.itemEmail.setText(dataModel.getEmail());
        holder.itemPhone.setText(dataModel.getPhone());
    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemSurname, itemEmail, itemPhone;

        public RecViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemSurname = itemView.findViewById(R.id.item_surname);
            itemEmail = itemView.findViewById(R.id.item_email);
            itemPhone = itemView.findViewById(R.id.item_phone);
        }
    }
}



