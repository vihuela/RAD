package worldgo.rad.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.lifecycle.NetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBaseBindingFragment;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.rad.app.RadApp;

/**
 * @author ricky.yao on 2017/3/23.
 * xml新增组件refresh All Gradle projects即可识别
 */

public abstract class BaseBindingFragment<T extends IView, R extends AbstractViewModel<T>, B extends ViewDataBinding> extends ViewModelBaseBindingFragment<T, R, B> {

    protected Context mContext;
    protected B mBinding;
    protected View mView;
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;
    //net queue
    public INetQueue mNetQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        mBinding = getBinding();
        mNetQueue = new NetQueue();

        onCreateView(savedInstanceState);
        setModelView((T) this);
        return mView;
    }

    public abstract ViewModelBindingConfig getViewModelBindingConfig();

    public abstract void onCreateView(@Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPrepare();
    }

    //首次可见
    protected abstract void onFirstUserVisible();

    //每次可见
    protected void onUserVisible() {
    }

    //首次不可见
    private void onFirstUserInvisible() {
    }

    //每次不可见
    protected void onUserInvisible() {
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // watch for memory leaks
        RefWatcher refWatcher = RadApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetQueue.cancel();
    }

    @Override
    public void showNetError(Error error, String content) {

    }

    @Override
    public void showMessage(String content) {

    }
}
