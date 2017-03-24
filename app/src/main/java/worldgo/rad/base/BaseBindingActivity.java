package worldgo.rad.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBaseBindingActivity;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;

/**
 * @author ricky.yao on 2017/3/23.
 */

public abstract class BaseBindingActivity<T extends IView, R extends AbstractViewModel<T>, B extends ViewDataBinding> extends ViewModelBaseBindingActivity<T, R, B> {


    protected B mBinding;
    protected Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = getBinding();
        onCreateView(savedInstanceState);
        setModelView((T) this);
    }

    public abstract void onCreateView(@Nullable Bundle savedInstanceState);

    public abstract ViewModelBindingConfig getViewModelBindingConfig();
}
