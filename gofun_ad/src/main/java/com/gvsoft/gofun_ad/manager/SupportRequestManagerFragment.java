package com.gvsoft.gofun_ad.manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashSet;
import java.util.Set;

public class SupportRequestManagerFragment extends Fragment {
    private static final String TAG = "SupportRequestManagerFragment";
    private final ActivityFragmentLifecycle lifecycle;
    private final Set<SupportRequestManagerFragment> childRequestManagerFragments = new HashSet<>();

    @Nullable private SupportRequestManagerFragment rootRequestManagerFragment;
    @Nullable private AdRequestManager requestManager;
    @Nullable private Fragment parentFragmentHint;

    public SupportRequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    public SupportRequestManagerFragment(@NonNull ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }


    public void setRequestManager(@Nullable AdRequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    ActivityFragmentLifecycle getGlideLifecycle() {
        return lifecycle;
    }

    @Nullable
    public AdRequestManager getRequestManager() {
        return requestManager;
    }


    private void addChildRequestManagerFragment(SupportRequestManagerFragment child) {
        childRequestManagerFragments.add(child);
    }

    private void removeChildRequestManagerFragment(SupportRequestManagerFragment child) {
        childRequestManagerFragments.remove(child);
    }

    /**
     * Sets a hint for which fragment is our parent which allows the fragment to return correct
     * information about its parents before pending fragment transactions have been executed.
     */
    void setParentFragmentHint(@Nullable Fragment parentFragmentHint) {
        this.parentFragmentHint = parentFragmentHint;
        if (parentFragmentHint != null && parentFragmentHint.getActivity() != null) {
            registerFragmentWithRoot(parentFragmentHint.getActivity());
        }
    }

    @Nullable
    private Fragment getParentFragmentUsingHint() {
        Fragment fragment = getParentFragment();
        return fragment != null ? fragment : parentFragmentHint;
    }

    private void registerFragmentWithRoot(@NonNull FragmentActivity activity) {
        unregisterFragmentWithRoot();
        rootRequestManagerFragment =
                GoFunAd.get(activity).getRequestManagerRetriever().getSupportRequestManagerFragment(activity);
        if (!equals(rootRequestManagerFragment)) {
            rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    private void unregisterFragmentWithRoot() {
        if (rootRequestManagerFragment != null) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            registerFragmentWithRoot(getActivity());
        } catch (IllegalStateException e) {
            // OnAttach can be called after the activity is destroyed, see #497.

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentFragmentHint = null;
        unregisterFragmentWithRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
        unregisterFragmentWithRoot();
    }

    @Override
    public String toString() {
        return super.toString() + "{parent=" + getParentFragmentUsingHint() + "}";
    }

}
