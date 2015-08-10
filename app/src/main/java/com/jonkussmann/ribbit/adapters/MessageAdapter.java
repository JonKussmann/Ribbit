package com.jonkussmann.ribbit.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonkussmann.ribbit.R;
import com.jonkussmann.ribbit.ui.ViewImageActivity;
import com.jonkussmann.ribbit.utils.ParseConstants;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jon Kussmann on 3/28/2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final String TAG = MessageAdapter.class.getSimpleName();

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_item, viewGroup, false);
        MessageViewHolder viewHolder = new MessageViewHolder(view);

        return viewHolder;
    }

    public void refill(List<ParseObject> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.bindMessage(mMessages.get(position));


    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView mMediaIcon;
        public TextView mSenderName;
        public TextView mTimeLabel;


        public MessageViewHolder(View itemView) {
            super(itemView);

            mMediaIcon = (ImageView) itemView.findViewById(R.id.messageIcon);
            mSenderName = (TextView) itemView.findViewById(R.id.senderLabel);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            itemView.setOnClickListener(this);
        }

        public void bindMessage(ParseObject object) {
            if (object.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
                mMediaIcon.setImageResource(R.drawable.ic_picture);
            } else {
                mMediaIcon.setImageResource(R.drawable.ic_video);
            }
            mSenderName.setText(object.getString(ParseConstants.KEY_SENDER_NAME));
            Date createdAt = object.getCreatedAt();
            long now = new Date().getTime();
            String convertedDate = DateUtils.getRelativeTimeSpanString(
                    createdAt.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS).toString();

            mTimeLabel.setText(convertedDate);


        }



        @Override
        public void onClick(View v) {
            Log.d(TAG, "Message item clicked on");
            int position = getAdapterPosition();
            ParseObject message = mMessages.get(position);
            String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
            ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);

            Uri fileUri = Uri.parse(file.getUrl());

            if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
                Intent intent = new Intent(mContext, ViewImageActivity.class);
                intent.setData(fileUri);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                intent.setDataAndType(fileUri, "video/*");
                mContext.startActivity(intent);

            }

            //Delete the message
            List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

            if (ids.size() == 1) {
                //last  recipient - delete the message
                message.deleteInBackground();
            } else {
                // remove the recipient and save
                ids.remove(ParseUser.getCurrentUser().getObjectId());


                ArrayList<String> idsToRemove = new ArrayList<String>();
                idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
                message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
                message.saveInBackground();

            }

        }
    }
}
