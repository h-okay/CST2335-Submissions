package algonquin.cst2335.okay0003.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.okay0003.R;
import algonquin.cst2335.okay0003.data.ChatMessage;
import algonquin.cst2335.okay0003.data.ChatRoomViewModel;
import algonquin.cst2335.okay0003.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.okay0003.databinding.SentMessageBinding;
import algonquin.cst2335.okay0003.databinding.ReceiveMessageBinding;


public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        ArrayList<ChatMessage> messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());
        }

        ArrayList<ChatMessage> finalMessages = messages; //why
        binding.sendButton.setOnClickListener(click -> {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(
                    binding.textInput.getText().toString(),
                    currentDateAndTime,
                    true
            );
            finalMessages.add(newMessage);
            myAdapter.notifyItemInserted(finalMessages.size() - 1);
            binding.textInput.setText("");
            chatModel.messages.postValue(finalMessages);
            binding.recycleView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));
        });

        binding.receiveButton.setOnClickListener(click -> {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(
                    binding.textInput.getText().toString(),
                    currentDateAndTime,
                    false
            );
            finalMessages.add(newMessage);
            myAdapter.notifyItemInserted(finalMessages.size() - 1);
            binding.textInput.setText("");
            chatModel.messages.postValue(finalMessages);
            binding.recycleView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ViewBinding binding;
                if (viewType == 0) {
                    binding = SentMessageBinding.inflate(getLayoutInflater());
                } else {
                    binding = ReceiveMessageBinding.inflate((getLayoutInflater()));
                }
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof MyRowHolder) {
                    MyRowHolder myRowHolder = (MyRowHolder) holder;
                    myRowHolder.messageText.setText("");
                    myRowHolder.timeText.setText("");

                    ChatMessage chatMessage = finalMessages.get(position);
                    myRowHolder.messageText.setText(chatMessage.getMessage());
                    myRowHolder.timeText.setText(chatMessage.getTimeSent());
                }
            }


            @Override
            public int getItemCount() {
                return finalMessages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage message = finalMessages.get(position);
                if (message.isSent()) {
                    return 0;
                }
                return 1;
            }
        });
    }
}

class MyRowHolder extends RecyclerView.ViewHolder {
    TextView messageText;
    TextView timeText;

    public MyRowHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.message);
        timeText = itemView.findViewById(R.id.time);
    }
}