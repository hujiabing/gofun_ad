package com.gvsoft.gofun_ad.manager;

import androidx.annotation.NonNull;

public interface Lifecycle {

    void addListener(@NonNull LifecycleListener listener);

    void removeListener(@NonNull LifecycleListener listener);
}
