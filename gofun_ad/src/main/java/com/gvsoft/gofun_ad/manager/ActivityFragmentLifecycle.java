package com.gvsoft.gofun_ad.manager;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.util.AdUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ActivityFragmentLifecycle implements Lifecycle {
    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new HashMap<LifecycleListener, Boolean>());

    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(@NonNull LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    @Override
    public void removeListener(@NonNull LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    void onStart() {
        isStarted = true;
        for (LifecycleListener lifecycleListener : AdUtils.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (LifecycleListener lifecycleListener : AdUtils.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : AdUtils.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }

}
