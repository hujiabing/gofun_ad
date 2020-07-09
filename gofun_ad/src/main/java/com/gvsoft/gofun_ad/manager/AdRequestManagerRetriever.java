package com.gvsoft.gofun_ad.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.gvsoft.gofun_ad.util.AdUtils;

import java.util.HashMap;
import java.util.Map;


public class AdRequestManagerRetriever implements Handler.Callback {

    private static final String TAG = "AdRMRetriever";
    static final String FRAGMENT_TAG = "com.gvsoft.gofun_ad.manager";
    private final Handler handler;
    private volatile AdRequestManager applicationManager;

    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments =
            new HashMap<>();

    public AdRequestManagerRetriever() {

        handler = new Handler(Looper.getMainLooper(), this /* Callback */);
    }


    @Override
    public boolean handleMessage(@NonNull Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) message.obj;
                key = supportFm;
                removed = pendingSupportRequestManagerFragments.remove(supportFm);
                break;
            default:
                handled = false;
                break;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }

    @NonNull
    public AdRequestManager get(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        return getApplicationManager(context);
    }


    @NonNull
    public AdRequestManager get(@NonNull FragmentActivity activity) {
        if (AdUtils.isOnBackgroundThread()) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            FragmentManager fm = activity.getSupportFragmentManager();
            return supportFragmentGet(
                    activity, fm, /*parentHint=*/ null, isActivityVisible(activity));
        }
    }

    @NonNull
    private AdRequestManager supportFragmentGet(
            @NonNull Context context,
            @NonNull FragmentManager fm,
            @Nullable Fragment parentHint,
            boolean isParentVisible) {
        SupportRequestManagerFragment current =
                getSupportRequestManagerFragment(fm, parentHint, isParentVisible);
        AdRequestManager requestManager = current.getRequestManager();
        if (requestManager == null) {
            // TODO(b/27524013): Factor out this Glide.get() call.
            GoFunAd glide = GoFunAd.get(context);
            requestManager =
                    DEFAULT_FACTORY.build(
                            glide, current.getGlideLifecycle(), context);
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }

    @NonNull
    SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentActivity activity) {
        return getSupportRequestManagerFragment(
                activity.getSupportFragmentManager(), /*parentHint=*/ null, isActivityVisible(activity));
    }

    @NonNull
    private SupportRequestManagerFragment getSupportRequestManagerFragment(
            @NonNull final FragmentManager fm, @Nullable Fragment parentHint, boolean isParentVisible) {
        SupportRequestManagerFragment current =
                (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingSupportRequestManagerFragments.get(fm);
            if (current == null) {
                current = new SupportRequestManagerFragment();
                current.setParentFragmentHint(parentHint);
                if (isParentVisible) {
                    current.getGlideLifecycle().onStart();
                }
                pendingSupportRequestManagerFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    private static boolean isActivityVisible(Activity activity) {
        // This is a poor heuristic, but it's about all we have. We'd rather err on the side of visible
        // and start requests than on the side of invisible and ignore valid requests.
        return !activity.isFinishing();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    @NonNull
    private AdRequestManager getApplicationManager(@NonNull Context context) {
        if (applicationManager == null) {
            synchronized (this) {
                if (applicationManager == null) {
                    GoFunAd glide = GoFunAd.get(context.getApplicationContext());
                    applicationManager =
                            DEFAULT_FACTORY.build(
                                    glide,
                                    new ApplicationLifecycle(),
                                    context.getApplicationContext());
                }
            }
        }
        return applicationManager;
    }

    public interface RequestManagerFactory {
        @NonNull
        AdRequestManager build(
                @NonNull GoFunAd goFunAd,
                @NonNull Lifecycle lifecycle,
                @NonNull Context context);
    }

    private static final RequestManagerFactory DEFAULT_FACTORY = new RequestManagerFactory() {

        @NonNull
        @Override
        public AdRequestManager build(@NonNull GoFunAd goFunAd,
                                      @NonNull Lifecycle lifecycle,
                                      @NonNull Context context) {
            return new AdRequestManager(goFunAd, lifecycle, context);
        }
    };
}
