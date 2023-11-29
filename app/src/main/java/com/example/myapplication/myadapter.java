package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {
        holder.header.setText(model.getTitle());
        holder.textviewbook.setText(model.getCategory());

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(holder.img1.getContext(),viewpdf.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("url",model.getUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
       return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView header;
        ImageView img1;
        TextView textviewbook;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            header=itemView.findViewById(R.id.header);
            textviewbook=itemView.findViewById(R.id.textviewbook);
            img1=itemView.findViewById(R.id.img1);

        }
    }
}
