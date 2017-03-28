package worldgo.common.viewmodel.framework;

import android.app.Activity;
import android.support.annotation.Nullable;

import worldgo.common.viewmodel.framework.base.view.BaseView;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;


/**
 * Any Activity or Fragment that needs a ViewModel needs to implement this interface.
 * You don't need to implement it yourself - use {@link worldgo.common.viewmodel.framework.base.ViewModelBaseActivity} and
 * {@link worldgo.common.viewmodel.framework.base.ViewModelBaseFragment} instead.
 */
public interface IView extends BaseView{
    /**
     * This method is used for Data Binding to bind correct layout and variable automatically
     * Can return null value in case that Data Binding is not used.
     *
     * @return defined ViewModelBinding Config for a specific screen.
     */
    @Nullable
    ViewModelBindingConfig getViewModelBindingConfig();

    /**
     * Implement this method to remove the ViewModel associated with the Fragment or Activity.
     * This is usually implemented by calling {@link ViewModelHelper#removeViewModel(Activity)},
     * see {@link worldgo.common.viewmodel.framework.base.ViewModelBaseActivity#removeViewModel()} and {@link worldgo.common.viewmodel.framework.base.ViewModelBaseFragment#removeViewModel()}.
     */
    void removeViewModel();
}
