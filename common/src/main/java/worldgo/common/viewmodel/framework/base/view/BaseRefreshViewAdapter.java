package worldgo.common.viewmodel.framework.base.view;

import android.support.annotation.Nullable;

import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;


public abstract class BaseRefreshViewAdapter<BEAN> implements  BaseRefreshView<BEAN>{
    @Override
    public void showLoading() {

    }

    @Override
    public void showNetError(Error error, String content) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showMessage(String content) {

    }

    @Override
    public void onRefreshData() {

    }

    @Nullable
    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return null;
    }

    @Override
    public void removeViewModel() {

    }
}
