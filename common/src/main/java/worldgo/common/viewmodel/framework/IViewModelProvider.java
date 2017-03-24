package worldgo.common.viewmodel.framework;

import android.support.annotation.Nullable;

/**
 * Your {@link android.app.Activity} must implement this interface if
 * any of the contained Fragments the {@link worldgo.common.viewmodel.framework.ViewModelHelper}
 */
public interface IViewModelProvider {

    /**
     * See {@link worldgo.common.viewmodel.framework.base.ViewModelBaseActivity} on how to implement.
     * @return the {@link ViewModelProvider}.
     */
    @Nullable
    ViewModelProvider getViewModelProvider();
}