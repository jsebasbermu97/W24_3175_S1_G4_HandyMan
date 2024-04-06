package com.example.handyman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handyman.adapters.MsgAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private DatabaseReference chatRef;
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private MsgAdapter messageAdapter;
    private List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // -------------- getting the data from the workerProfileActivity --------------
        String workerName = getIntent().getStringExtra("workerName");
        String workerProfession = getIntent().getStringExtra("workerProfession");
        String ownerName = getIntent().getStringExtra("ownerName");

        // -------------- getting reference to the Firebase database --------------
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        chatRef = database.getReference("Messages/ " + ownerName+ "/" +workerName );

        // -------------- getting the views of this activity --------------
        TextView textViewPersonName = findViewById(R.id.txtViewPersonNameChat);
        recyclerViewChat = findViewById(R.id.RecyclerViewChat);
        editTextMessage = findViewById(R.id.EditTextMessage);
        Button btnSendMessage = findViewById(R.id.BtnSendMessage);
        ImageView imgViewProfession = findViewById(R.id.imageViewWorkerProfile);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType","");

        switch (userType) {
            case "worker":
                textViewPersonName.setText(ownerName);
                textViewPersonName.setGravity(Gravity.CENTER);
                break;
            case "owner":
                textViewPersonName.setText(workerName);
                textViewPersonName.setGravity(Gravity.CENTER);
                break;
            default:
                return;
        }

        // -------------- displaying the image based on the profession --------------
        switch (workerProfession) {
            case "Carpenter":
                imgViewProfession.setImageResource(R.drawable.carpenter);
                break;
            case "Electrician":
                imgViewProfession.setImageResource(R.drawable.electrician);
                break;
            case "Plumber":
                imgViewProfession.setImageResource(R.drawable.plumber);
                break;
            case "Painter":
                imgViewProfession.setImageResource(R.drawable.painter);
                break;
            default:
                return;
        }

        // -------------- configuring the recycler view --------------
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);

        switch (userType) {
            case "worker":
                messageAdapter = new MsgAdapter(messages, workerName);
                break;
            case "owner":
                messageAdapter = new MsgAdapter(messages, ownerName);
                break;
            default:
                return;
        }
        recyclerViewChat.setAdapter(messageAdapter);

        // -------------- send Message Button --------------
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // -------------- listen to the initial set of messages --------------
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String message = snapshot.getValue(String.class);
                    messages.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                recyclerViewChat.scrollToPosition(messages.size() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // -------------- defining the send message function --------------
    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {

            String messageKey = chatRef.push().getKey(); // generating a unique key for the message
            chatRef.child(messageKey).setValue(message); // storing the message in the db under the key generated
            editTextMessage.setText(""); // clearing the text field after sending the message
            messages.add(message);
            messageAdapter.notifyItemInserted(messages.size()-1);
            recyclerViewChat.scrollToPosition(messages.size() - 1);
        } else {
            Toast.makeText(ChatActivity.this, "Type a message", Toast.LENGTH_SHORT).show();
        }

    }
}