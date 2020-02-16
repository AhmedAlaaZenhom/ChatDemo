package com.intcore.chatdemo.chat_internal.custom_view_holders;

import android.view.View;

import com.intcore.chatdemo.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;

public class OutcomingTextMessageCustomViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<MessageModel> {

    public OutcomingTextMessageCustomViewHolder(View itemView) {
        super(itemView, null);
    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);
    }
}
