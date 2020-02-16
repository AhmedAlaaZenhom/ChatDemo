package com.intcore.chatdemo.chat_internal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.intcore.chatdemo.R;
import com.intcore.chatdemo.Utils.PicassoHelper;
import com.intcore.chatdemo.chat_internal.custom_view_holders.IncomingTextMessageCustomViewHolder;
import com.intcore.chatdemo.chat_internal.custom_view_holders.IncomingVoiceMessageViewHolder;
import com.intcore.chatdemo.chat_internal.custom_view_holders.OutcomingTextMessageCustomViewHolder;
import com.intcore.chatdemo.chat_internal.custom_view_holders.OutcomingVoiceMessageViewHolder;
import com.intcore.chatdemo.model.BuddyModel;
import com.intcore.chatdemo.model.MessageModel;
import com.intcore.chatdemo.model.MessageType;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatInternalActivity extends AppCompatActivity {
    private static final String TAG = ChatInternalActivity.class.getSimpleName();

    private static final String DATA = "DATA";

    public static Intent getActivityIntent(Context context, BuddyModel buddyModel) {
        Intent intent = new Intent(context, ChatInternalActivity.class);
        intent.putExtra(DATA, buddyModel);
        return intent;
    }

    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.rv_messages_list)
    MessagesList rvMessagesList;
    @BindView(R.id.layout_message_input)
    MessageInput layoutMessageInput;
    @BindView(R.id.switch_user_mode)
    Switch switchUserMode;
    @BindView(R.id.iv_record)
    ImageView iv_record;

    private BuddyModel user;
    private BuddyModel currentBuddy;

    private MessagesListAdapter<MessageModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_internal);
        ButterKnife.bind(this);

        if (!getDataFromIntent())
            return;

        setUpViews();

        getFakeMessagesList();
    }

    private boolean getDataFromIntent() {
        currentBuddy = (BuddyModel) getIntent().getSerializableExtra(DATA);
        if (currentBuddy == null) {
            // show error
            finish();
            return false;
        }
        user = new BuddyModel("0", null, null);
        return true;
    }

    private void setUpViews() {
        // Toolbar
        toolbar.findViewById(R.id.iv_back).setOnClickListener(view -> onBackPressed());
        ((TextView) toolbar.findViewById(R.id.tv_buddy_name)).setText(currentBuddy.getName());
        PicassoHelper.loadImageWithCache(currentBuddy.getAvatar(), toolbar.findViewById(R.id.iv_buddy_avatar), PicassoHelper.JUST_FIT, null, null);

        // Message input
        layoutMessageInput.findViewById(R.id.messageInput)
                .setPadding(
                        (int) getResources().getDimension(R.dimen.input_text_padding_left),
                        (int) getResources().getDimension(R.dimen.input_text_padding_top),
                        (int) getResources().getDimension(R.dimen.input_text_padding_right),
                        (int) getResources().getDimension(R.dimen.input_text_padding_bottom
                        ));
        layoutMessageInput.setInputListener(input -> {
            String body = input.toString().trim();
            if (!TextUtils.isEmpty(body)) {
                MessageModel messageModel = new MessageModel(String.valueOf(System.currentTimeMillis()), body, switchUserMode.isChecked() ? user : currentBuddy, new Date());
                adapter.addToStart(messageModel, true);
            }
            return !TextUtils.isEmpty(body);
        });
        layoutMessageInput.setAttachmentsListener(() -> {
            // TODO: Show add sheet instead of gallery
            chooseImageFromGallery();
        });

        // Voice recording
        iv_record.setOnClickListener(v -> {
            MessageModel messageModel = new MessageModel(String.valueOf(System.currentTimeMillis()), "", switchUserMode.isChecked() ? user : currentBuddy, new Date(), MessageType.VOICE);
            adapter.addToStart(messageModel, true);
        });

        // Message list
        MessageHolders holders = new MessageHolders();

        holders.setIncomingTextConfig(IncomingTextMessageCustomViewHolder.class, R.layout.item_incoming_text_message);

        holders.setOutcomingTextConfig(OutcomingTextMessageCustomViewHolder.class, R.layout.item_outcoming_text_message);

        holders.registerContentType(Integer.valueOf(MessageType.VOICE).byteValue(),
                IncomingVoiceMessageViewHolder.class, R.layout.item_custom_incoming_voice_message,
                OutcomingVoiceMessageViewHolder.class, R.layout.item_custom_outcoming_voice_message,
                (message, type) -> ((MessageModel) message).getType() == MessageType.VOICE);

        adapter = new MessagesListAdapter<>(user.getId(), holders,
                (imageView, url, payload) -> Picasso.get().load(url).into(imageView));

        adapter.setLoadMoreListener((page, totalItemsCount) -> {
            // TODO: Handle pagination
        });
        adapter.setOnMessageClickListener(message -> {
            if (message.getType() == MessageType.IMAGE) {
                new StfalconImageViewer.Builder<>(ChatInternalActivity.this,
                        new String[]{message.getImageUrl()},
                        (imageView, image) -> Picasso.get().load(image).into(imageView)).show();
            }
        });
        rvMessagesList.setAdapter(adapter);
    }

    private void chooseImageFromGallery() {
        RxPaparazzo.single(this)
                .size(new CustomMaxSize(500))
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    createGalleryImageMessage(response.data());
                });
    }

    private void createGalleryImageMessage(FileData fileData) {
        MessageModel model = new MessageModel(
                String.valueOf(System.currentTimeMillis()),
                "file://" + fileData.getFile(),
                switchUserMode.isChecked() ? user : currentBuddy,
                new Date(),
                MessageType.IMAGE);
        Picasso.get().invalidate(model.getImageUrl());
        adapter.addToStart(model, true);
    }


    private void getFakeMessagesList() {
        MessageModel message1 = new MessageModel("1", "Hello there!", user, new Date());
        MessageModel message2 = new MessageModel("2", "Hey, how are you doing ?", currentBuddy, new Date());
        MessageModel message3 = new MessageModel("3", "Fine, what about you ?", user, new Date());
        MessageModel message4 = new MessageModel("4", "I'm fine. So how is everything going at work ?", currentBuddy, new Date());
        MessageModel message5 = new MessageModel("5", "Good, Actually everything is great :)", user, new Date());
        MessageModel message6 = new MessageModel("6", "Good to hear", currentBuddy, new Date());
        MessageModel message7 = new MessageModel("7", "You available tomorrow ?", currentBuddy, new Date());
        MessageModel message8 = new MessageModel("8", "Yeah, most of the day", user, new Date());
        MessageModel message9 = new MessageModel("9", "May i ask why ?!", user, new Date());
        MessageModel message10 = new MessageModel("7", "I was thinking if we could hangout maybe ...", currentBuddy, new Date());


        List<MessageModel> messageList = new ArrayList<>();
        messageList.add(message1);
        messageList.add(message2);
        messageList.add(message3);
        messageList.add(message4);
        messageList.add(message5);
        messageList.add(message6);
        messageList.add(message7);
        messageList.add(message8);
        messageList.add(message9);
        messageList.add(message10);

        adapter.addToEnd(messageList, true);
    }
}
