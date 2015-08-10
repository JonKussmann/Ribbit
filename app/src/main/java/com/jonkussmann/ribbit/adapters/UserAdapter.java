package com.jonkussmann.ribbit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonkussmann.ribbit.R;
import com.jonkussmann.ribbit.utils.MD5Util;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jon Kussmann on 3/28/2015.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MessageViewHolder> {
    private static final String TAG = UserAdapter.class.getSimpleName();

    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> messages) {
        mContext = context;
        mUsers = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false);
        MessageViewHolder viewHolder = new MessageViewHolder(view);

        return viewHolder;
    }

    public void refill(List<ParseUser> messages) {
        mUsers.clear();
        mUsers.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(UserAdapter.MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.bindMessage(mUsers.get(position));


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameLabel;
        public ImageView userImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);

            nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            userImageView = (ImageView)itemView.findViewById(R.id.userImageView);
            itemView.setOnClickListener(this);
        }

        public void bindMessage(ParseUser user) {
//            if (object.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
//                mMediaIcon.setImageResource(R.drawable.ic_picture);
//            } else {
//                mMediaIcon.setImageResource(R.drawable.ic_video);
//            }
            Log.d(TAG, user.getUsername());
            nameLabel.setText(user.getUsername());

            String email = user.getEmail().toLowerCase();
            if (email.equals("")) {
                userImageView.setImageResource(R.drawable.avatar_empty);
            } else {
                String hash = MD5Util.md5Hex(email);
                String gravatarUrl = "http://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
                Log.d(TAG, gravatarUrl);

                Picasso.with(mContext)
                        .load(gravatarUrl)
                        .placeholder(R.drawable.avatar_empty)
                        .into(userImageView);
            }


        }


        @Override
        public void onClick(View v) {

        }
    }
}
