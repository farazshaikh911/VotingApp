package com.example.voting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
   ArrayList<Members> memberArrayList = new ArrayList<>();
   Context context;
   boolean isAdmin;
   onClick onClick;
    private static int lastClickedPosition = -1;

   public MemberAdapter(Context context, ArrayList<Members> memberArrayList,boolean isAdmin,onClick onClick){
       this.context = context;
       this.memberArrayList = memberArrayList;
       this.isAdmin = isAdmin;
       this.onClick = onClick;

   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_member_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_memberName.setText(memberArrayList.get(position).getName());
        if(isAdmin) {
            holder.tv_memberCount.setVisibility(View.VISIBLE);
            holder.tv_memberCount.setText(String.valueOf(memberArrayList.get(position).getCount()));
        }
        else{
            holder.cl_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(memberArrayList.get(holder.getAdapterPosition()));
                    lastClickedPosition=holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            if(lastClickedPosition==position){
                holder.cl_member.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.material_grey_300));
            }
            else
            {
                holder.cl_member.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.m3_ref_palette_white));
            }
        }
        }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView tv_memberName,tv_memberCount;
       ConstraintLayout cl_member;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_memberName = itemView.findViewById(R.id.tv_memberName);
            tv_memberCount = itemView.findViewById(R.id.tv_memberCount);
            cl_member = itemView.findViewById(R.id.cl_member);
        }
    }

}
