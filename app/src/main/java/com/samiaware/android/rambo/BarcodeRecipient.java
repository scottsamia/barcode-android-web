package com.samiaware.android.rambo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SamiaS on 3/11/2016.
 */
public interface BarcodeRecipient {

    void onReceive(String barcode, String barcodeType);
}
