package com.intcore.chatdemo.chat_list;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intcore.chatdemo.R;
import com.intcore.chatdemo.chat_internal.ChatInternalActivity;
import com.intcore.chatdemo.model.BuddyModel;
import com.intcore.chatdemo.model.ChatModel;
import com.intcore.chatdemo.model.MessageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListActivity extends AppCompatActivity {

    @BindView(R.id.rv_chat_list)
    RecyclerView rvChatList;

    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);
        ButterKnife.bind(this);

        setUpViews();

        getFakeChatList();
    }

    private void setUpViews() {
        chatListAdapter = new ChatListAdapter(this, model -> {
            if (model.getBuddy() != null)
                startActivity(ChatInternalActivity.getActivityIntent(this, (BuddyModel) model.getBuddy()));
        });
        rvChatList.setAdapter(chatListAdapter);
        rvChatList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getFakeChatList() {
        List<ChatModel> chatList = new ArrayList<>() ;

        BuddyModel buddyModel1 = new BuddyModel("1", "Ahmed Alaa", "https://avatars1.githubusercontent.com/u/650755?v=3&s=400");
        BuddyModel buddyModel2 = new BuddyModel("2", "Hassan Ashraf", "https://avatars1.githubusercontent.com/u/650756?v=3&s=400");
        BuddyModel buddyModel3 = new BuddyModel("3", "Ahmed Gamal", "https://avatars1.githubusercontent.com/u/650757?v=3&s=400");
        BuddyModel buddyModel4 = new BuddyModel("4", "George Sami", "https://avatars1.githubusercontent.com/u/650758?v=3&s=400");
        BuddyModel buddyModel5 = new BuddyModel("5", "Dean Winchester", "https://avatars1.githubusercontent.com/u/650759?v=3&s=400");
        BuddyModel buddyModel6 = new BuddyModel("6", "Darth Vader", "https://avatars1.githubusercontent.com/u/650760?v=3&s=400");

        MessageModel messageModel1 = new MessageModel("1",getString(R.string.lorem_ipsum), buddyModel1, new Date());
        MessageModel messageModel2 = new MessageModel("2",getString(R.string.lorem_ipsum), buddyModel2, new Date());
        MessageModel messageModel3 = new MessageModel("3",getString(R.string.lorem_ipsum), buddyModel3, new Date());
        MessageModel messageModel4 = new MessageModel("4",getString(R.string.lorem_ipsum), buddyModel4, new Date());
        MessageModel messageModel5 = new MessageModel("5",getString(R.string.lorem_ipsum), buddyModel5, new Date());
        MessageModel messageModel6 = new MessageModel("6",getString(R.string.lorem_ipsum), buddyModel6, new Date());

        ChatModel chatModel1 = new ChatModel("1", buddyModel1, messageModel1, 3);
        ChatModel chatModel2 = new ChatModel("2", buddyModel2, messageModel2, 2);
        ChatModel chatModel3 = new ChatModel("3", buddyModel3, messageModel3, 1);
        ChatModel chatModel4 = new ChatModel("4", buddyModel4, messageModel4, 0);
        ChatModel chatModel5 = new ChatModel("5", buddyModel5, messageModel5, 0);
        ChatModel chatModel6 = new ChatModel("6", buddyModel6, messageModel6, 0);

        chatList.add(chatModel1);
        chatList.add(chatModel2);
        chatList.add(chatModel3);
        chatList.add(chatModel4);
        chatList.add(chatModel5);
        chatList.add(chatModel6);

        chatListAdapter.addAll(chatList);
    }
}
