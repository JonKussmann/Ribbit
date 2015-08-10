package com.jonkussmann.ribbit.utils;

import android.content.Context;
import android.content.Intent;

import com.jonkussmann.ribbit.ui.MainActivity;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Jon Kussmann on 3/31/2015.
 */
public class ParseReceiver extends ParsePushBroadcastReceiver{
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);

        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
