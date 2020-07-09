package com.gvsoft.gofun_ad.manager;

import androidx.annotation.NonNull;


class ApplicationLifecycle implements Lifecycle {
    @Override
    public void addListener(@NonNull LifecycleListener listener) {
        listener.onStart();
    }

    @Override
    public void removeListener(@NonNull LifecycleListener listener) {
        // Do nothing.
    }
}
