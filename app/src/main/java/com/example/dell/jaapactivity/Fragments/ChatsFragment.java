package com.example.dell.jaapactivity.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.jaapactivity.Adapter.UserAdapter;
import com.example.dell.jaapactivity.Model.Chat;
import com.example.dell.jaapactivity.Model.User;
import com.example.dell.jaapactivity.R;
import com.example.dell.jaapactivity.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ChatsFragment extends Fragment {

    public RecyclerView recyclerView;
    public UserAdapter userAdapter;
    public List<Users> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    public List<String> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_chats, container, false);

       recyclerView = view.findViewById(R.id.recycler_view_fr);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       usersList = new ArrayList<>();
       reference = FirebaseDatabase.getInstance().getReference("Chats");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               usersList.clear();
               for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   Chat chat = snapshot.getValue(Chat.class);
                   if(chat.getSender().equals(firebaseUser.getUid())){
                       usersList.add(chat.getReceiver());
                   }
                   if(chat.getReceiver().equals(firebaseUser.getUid())){
                       usersList.add(chat.getSender());

                   }
               }
               readChats();

           }


           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       return view;
    }

    public void readChats() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    try{
                        for(String id : usersList){
                            if(user.getId().equals(id)){
                                if(mUsers.size() != 0){
                                    for(Users user1 : mUsers){
                                        if(!user.getId().equals(user1.getId())){
                                            mUsers.add(user);
                                        }
                                    }
                                }else{
                                    mUsers.add(user);
                                }
                            }
                        }
                    }catch (ConcurrentModificationException e){
                        Log.d(TAG, "onDataChange: ");
                    }

                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
