package com.intcore.chatdemo.chat_list;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.intcore.chatdemo.R;
import com.intcore.chatdemo.Utils.PicassoHelper;
import com.intcore.chatdemo.Utils.Utils;
import com.intcore.chatdemo.base.BaseRecyclerAdapter;
import com.intcore.chatdemo.base.ItemClickListener;
import com.intcore.chatdemo.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>
        implements BaseRecyclerAdapter<ChatModel> {

    private FragmentActivity activity;
    private List<ChatModel> list;
    private ItemClickListener<ChatModel> listener;

    private int width, height, spacing, halfSpacing;

    public ChatListAdapter(FragmentActivity activity, ItemClickListener<ChatModel> listener) {
        this.activity = activity;
        this.listener = listener;

        this.list = new ArrayList<>();

        spacing = (int) activity.getResources().getDimension(R.dimen.padding);
        halfSpacing = (int) activity.getResources().getDimension(R.dimen.half_padding);
        width = Utils.getScreenDisplayMetrics(activity).widthPixels - 2 * spacing;
        height = (int) (width * 0.27);
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.bindData(position);
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent)
        ConstraintLayout parent;
        @BindView(R.id.iv_chat_buddy_image)
        ImageView ivChatBuddyImage;
        @BindView(R.id.tv_chat_buddy_name)
        TextView tvChatBuddyName;
        @BindView(R.id.tv_chat_last_message_body)
        TextView tvChatLastMessageBody;
        @BindView(R.id.tv_chat_last_message_date)
        TextView tvChatLastMessageDate;

        ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(int position) {
            ChatModel model = list.get(position);


            // Setting parent layoutParams
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);
            layoutParams.setMarginStart(spacing);
            layoutParams.setMarginEnd(spacing);
            layoutParams.topMargin = position == 0 ? spacing : halfSpacing;
            layoutParams.bottomMargin = position == list.size() - 1 ? spacing : 0;
            parent.setLayoutParams(layoutParams);

            // Setting parent click listener
            parent.setOnClickListener(view -> {
                if (listener != null)
                    listener.onItemClicked(model);
            });

            // Binding Data
            if (model.getBuddy() != null) {
                tvChatBuddyName.setText(model.getBuddy().getName());
                PicassoHelper.loadImageWithCache(model.getBuddy().getAvatar(), ivChatBuddyImage, PicassoHelper.CENTER_INSIDE, null, null);
            }
            if (model.getLastMessage() != null) {
                tvChatLastMessageBody.setText(model.getLastMessage().getText());
                tvChatLastMessageDate.setText("02:04 PM");
            }

            tvChatLastMessageBody.setTypeface(model.getUnreadCount() > 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        }
    }

    @Override
    public void clear(boolean notifyDataSetChanged) {
        list.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public void add(ChatModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<ChatModel> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

}