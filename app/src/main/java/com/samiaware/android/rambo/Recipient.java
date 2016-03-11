package com.samiaware.android.rambo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SamiaS on 3/11/2016.
 */
public interface Recipient {

    void onReceive(Context context, Intent intent);
}
