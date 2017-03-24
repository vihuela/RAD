package worldgo.common.viewmodel.framework.binding;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import worldgo.common.viewmodel.aop.entity.MPermissionUtils;
import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.base.ViewModelBaseFragment;


public abstract class ViewModelBaseBindingFragment<T extends IView, R extends AbstractViewModel<T>, B extends ViewDataBinding>
        extends ViewModelBaseFragment<T, R>
        implements IView {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModelHelper().performBinding(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getViewModelHelper().performBinding(this);
        final ViewDataBinding binding = getViewModelHelper().getBinding();
        if (binding != null) {
            return binding.getRoot();
        } else {
            throw new IllegalStateException("Binding cannot be null. Perform binding before calling getBinding()");
        }
    }

    @SuppressWarnings("unused")
    @NonNull
    public B getBinding() {
        try {
            return (B) getViewModelHelper().getBinding();
        } catch (ClassCastException ex) {
            throw new IllegalStateException("Method getViewModelBindingConfig() has to return same " +
                    "ViewDataBinding type as it is set to base Fragment");
        }
    }

}
