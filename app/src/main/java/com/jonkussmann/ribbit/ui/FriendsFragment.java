package com.jonkussmann.ribbit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonkussmann.ribbit.R;
import com.jonkussmann.ribbit.adapters.UserAdapter;
import com.jonkussmann.ribbit.utils.AutoRecyclerView;
import com.jonkussmann.ribbit.utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Jon Kussmann on 3/26/2015.
 */
public class FriendsFragment extends Fragment {
    private static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected AutoRecyclerView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        mGridView = (AutoRecyclerView)rootView.findViewById(R.id.recyclerView);
//        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
//        mGridView.setLayoutManager(layoutManager);

        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        getActivity().setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    Log.d(TAG, "Friends list size: " + mFriends.size());
                    if (mGridView.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
                        mGridView.setAdapter(adapter);

                        Log.d(TAG, "creating the adapter");
                    } else {
                        // refill the adapter
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                        Log.d(TAG, "refill the adapter");

                    }
                } else {
                    Log.e(TAG, e.getMessage());
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage());
                    builder.setTitle(getString(R.string.error_title) + 2);
                    builder.setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                }
            }
        });
    }
}
