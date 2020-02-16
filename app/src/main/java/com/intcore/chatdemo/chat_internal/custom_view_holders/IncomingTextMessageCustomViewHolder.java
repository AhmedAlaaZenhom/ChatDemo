package com.intcore.chatdemo.chat_internal.custom_view_holders;

import android.view.View;

import com.intcore.chatdemo.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;

public class IncomingTextMessageCustomViewHolder extends MessageHolders.IncomingTextMessageViewHolder<MessageModel> {

    public IncomingTextMessageCustomViewHolder(View itemView) {
        super(itemView, null);
    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);
    }
}
