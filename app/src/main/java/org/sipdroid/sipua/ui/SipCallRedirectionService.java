package org.sipdroid.sipua.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.telecom.CallRedirectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;

@TargetApi(29)
public class SipCallRedirectionService extends CallRedirectionService {

    @Override
    public void onPlaceCall(Uri handle, PhoneAccountHandle initialPhoneAccount, boolean allowInteractiveResponse) {
        String number = handle.getSchemeSpecificPart();
        Log.i("SipUA", "SipCallRedirectionService intercepting: " + number);

        if (!Sipdroid.on(this)) {
            placeCallUnmodified();
            return;
        }

        android.content.SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("bypass_redirection", false)) {
            prefs.edit().putBoolean("bypass_redirection", false).commit();
            placeCallUnmodified();
            return;
        }

        String pref = prefs.getString(org.sipdroid.sipua.ui.Settings.PREF_PREF, org.sipdroid.sipua.ui.Settings.DEFAULT_PREF);

        if (pref.equals(org.sipdroid.sipua.ui.Settings.VAL_PREF_PSTN)) {
            placeCallUnmodified();
            return;
        }
        
        if (pref.equals(org.sipdroid.sipua.ui.Settings.VAL_PREF_SIP)) {
            if (org.sipdroid.sipua.ui.Receiver.mSipdroidEngine == null || !org.sipdroid.sipua.ui.Receiver.mSipdroidEngine.isRegistered()) {
                placeCallUnmodified();
                return;
            }
        }

        // Start Sipdroid's call handler activity FIRST to utilize the CallRedirectionService
        // background activity launch exemption.
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("sipdroid", Uri.decode(number), null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Cancel the native cellular call.
        cancelCall();
    }
}
