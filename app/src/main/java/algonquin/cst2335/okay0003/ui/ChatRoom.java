package algonquin.cst2335.okay0003.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewbinding.ViewBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.okay0003.R;
import algonquin.cst2335.okay0003.data.ChatMessage;
import algonquin.cst2335.okay0003.data.ChatMessageDAO;
import algonquin.cst2335.okay0003.data.ChatRoomViewModel;
import algonquin.cst2335.okay0003.data.MessageDatabase;
import algonquin.cst2335.okay0003.data.MessageDetailsFragment;
import algonquin.cst2335.okay0003.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.okay0003.databinding.SentMessageBinding;
import algonquin.cst2335.okay0003.databinding.ReceiveMessageBinding;


public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                messages.addAll(mDAO.getAllMessages());
                chatModel.messages.postValue(messages);
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });
        }

        binding.sendButton.setOnClickListener(click -> {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage newMessage = new ChatMessage(
                    binding.textInput.getText().toString(),
                    currentDateAndTime,
                    true
            );
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
            chatModel.messages.postValue(messages);
            binding.recycleView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> mDAO.insertMessage(newMessage));
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
            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
            chatModel.messages.postValue(messages);
            binding.recycleView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> mDAO.insertMessage(newMessage));
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

                    ChatMessage chatMessage = messages.get(position);
                    myRowHolder.messageText.setText(chatMessage.getMessage());
                    myRowHolder.timeText.setText(chatMessage.getTimeSent());
                }
            }


            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage message = messages.get(position);
                if (message.isSent()) {
                    return 0;
                }
                return 1;
            }
        });

        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragmentLocation, chatFragment); // had to replace so it is refreshed
            tx.addToBackStack("");
            tx.commit();
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                itemView.setOnClickListener(click -> {
                    int position = getAbsoluteAdapterPosition();
                    ChatMessage selected = messages.get(position);
                    chatModel.selectedMessage.postValue(selected);
                });
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}

