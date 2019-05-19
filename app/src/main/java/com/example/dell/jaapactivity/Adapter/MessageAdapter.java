package com.example.dell.jaapactivity.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.jaapactivity.Model.Chat;
import com.example.dell.jaapactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;

    public static final int  MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser firebaseUser;



    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageUrl = imageUrl;


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
        return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        if(imageUrl.equals("default")){
            viewHolder.profile_image.setImageResource(R.drawable.boy);

        }
        else {
            Glide.with(mContext).load(imageUrl).into(viewHolder.profile_image);

        }


    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message =itemView.findViewById(R.id.show_message);
            Log.d(TAG, "ViewHolder: "+ show_message);
            profile_image = itemView.findViewById(R.id.profile_image);

        }

    }



    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}

