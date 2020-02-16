package com.intcore.chatdemo.chat_internal.custom_view_holders;

import android.view.View;
import android.widget.TextView;

import com.intcore.chatdemo.R;
import com.intcore.chatdemo.model.MessageModel;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

public class OutcomingVoiceMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<MessageModel> {

    private TextView tvDuration;
    private TextView tvTime;

    public OutcomingVoiceMessageViewHolder(View itemView) {
        super(itemView, null);
        tvDuration = (TextView) itemView.findViewById(R.id.duration);
        tvTime = (TextView) itemView.findViewById(R.id.time);
    }

    @Override
    public void onBind(MessageModel message) {
        super.onBind(message);
        tvDuration.setText("2:04");
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}
