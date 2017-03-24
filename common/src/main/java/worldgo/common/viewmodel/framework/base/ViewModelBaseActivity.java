package worldgo.common.viewmodel.framework.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.IViewModelFactory;
import worldgo.common.viewmodel.framework.ProxyViewHelper;
import worldgo.common.viewmodel.framework.ViewModelHelper;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;


public abstract class ViewModelBaseActivity<T extends IView, R extends AbstractViewModel<T>> extends ViewModelBaseEmptyActivity implements IView {

    @NonNull
    protected final ViewModelHelper<T, R> mViewModeHelper = new ViewModelHelper<>();

    @CallSuper
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IViewModelFactory<T, R> viewModelFactory = getViewModelFactory();
        // try to extract the ViewModel class from the implementation
        if (viewModelFactory == null) {
            //noinspection unchecked
            final Class<? extends AbstractViewModel<T>> viewModelClass = (Class<? extends AbstractViewModel<T>>) ProxyViewHelper.getGenericType(getClass(), AbstractViewModel.class);
            if (viewModelClass != null) {
                viewModelFactory = new IViewModelFactory<T, R>() {
                    @Nullable
                    @Override
                    public R createViewModel() {
                        try {
                            return (R) viewModelClass.newInstance();
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        }
        mViewModeHelper.onCreate(this, savedInstanceState, viewModelFactory, getIntent().getExtras());
    }

    /**
     * Call this after your view is ready - usually on the end of {@link android.app.Activity#onCreate(Bundle)}
     *
     * @param view view
     */
    @SuppressWarnings("unused")
    public void setModelView(@NonNull final T view) {
        mViewModeHelper.setView(view);
    }

    @Nullable
    public IViewModelFactory<T, R> getViewModelFactory() {
        return null;
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModeHelper.onSaveInstanceState(outState);
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        mViewModeHelper.onStart();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        mViewModeHelper.onStop();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mViewModeHelper.onDestroy(this);
        super.onDestroy();
    }

    /**
     * @see ViewModelHelper#getViewModel()
     */
    @SuppressWarnings("unused")
    @NonNull
    public R getViewModel() {
        return mViewModeHelper.getViewModel();
    }


    @Override
    public void removeViewModel() {
        mViewModeHelper.removeViewModel(this);
    }

    @Nullable
    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return null;
    }
}
