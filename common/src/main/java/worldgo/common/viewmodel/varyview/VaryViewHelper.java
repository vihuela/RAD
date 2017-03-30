/*
 * Copyright (c) 2016. WorldGo Technology Co., Ltd
 * DO NOT DIVULGE
 */

package worldgo.common.viewmodel.varyview;

import android.text.TextUtils;
import android.view.View;

import worldgo.common.R;


/**
 * 功能：帮助切换错误，数据为空，正在加载的页面
 */
public class VaryViewHelper {
    /**
     * 切换不同视图的帮助类
     */
    private OverlapViewHelper mViewHelper;
    /**
     * 错误页面
     */
    private View mErrorView;
    /**
     * 正在加载页面
     */
    private View mLoadingView;
    /**
     * 数据为空的页面
     */
    private View mEmptyView;
    /**
     * 正在加载页面的进度环
     */
    private ProgressWheel mLoadingProgress;

    /**
     * 开放的设置
     */
    private BaseVaryViewSetter mViewSetter;


    private VaryViewHelper(View view) {
        this(new OverlapViewHelper(view));
    }

    private VaryViewHelper(OverlapViewHelper helper) {
        this.mViewHelper = helper;
    }


    private void setUpEmptyView(View view) {
        mEmptyView = view;
        mEmptyView.setClickable(true);
    }

    private void setUpErrorView(View view, View.OnClickListener listener) {
        mErrorView = view;
        mErrorView.setClickable(true);

        View btn = view.findViewById(R.id.vv_error_refresh);
        if (btn != null) {
            btn.setOnClickListener(listener);
        }
    }

    private void setUpLoadingView(View view) {
        mLoadingView = view;
        mLoadingView.setClickable(true);
        mLoadingProgress = (ProgressWheel) view.findViewById(R.id.vv_loading_progress);
    }

    /**
     * --------------------------------------------------------------------------------------
     */
    public void showEmptyView() {
        mViewHelper.showCaseLayout(mEmptyView);
        stopProgressLoading();
    }

    public void showErrorView() {
        mViewHelper.showCaseLayout(mErrorView);
        stopProgressLoading();
    }

    public void showErrorView(String message) {
        if (!TextUtils.isEmpty(message)) {
            BaseVaryViewSetter.setText(mErrorView, R.id.error_tips_show, message);
        }
        mViewHelper.showCaseLayout(mErrorView);
        stopProgressLoading();
    }

    public void showLoadingView() {
        mViewHelper.showCaseLayout(mLoadingView);
        startProgressLoading();
    }

    public void showDataView() {
        mViewHelper.restoreLayout();
        stopProgressLoading();
    }

    /**
     * -------------------------------------------------------------------------------------------
     */

    private void stopProgressLoading() {
        if (mLoadingProgress != null && mLoadingProgress.isSpinning()) {
            mLoadingProgress.stopSpinning();
        }
    }

    private void startProgressLoading() {
        if (mLoadingProgress != null && !mLoadingProgress.isSpinning()) {
            mLoadingProgress.spin();
        }
    }

    public void releaseVaryView() {
        mErrorView = null;
        mLoadingView = null;
        mEmptyView = null;
    }


    public void setViewSetter(BaseVaryViewSetter viewSetter) {
        mViewSetter = viewSetter;
        if (mLoadingView != null) {
            BaseVaryViewSetter.setText(mLoadingView, R.id.loading_tips_show, mViewSetter.getLoadingContent());
        }
        if (mEmptyView != null) {
            BaseVaryViewSetter.setText(mEmptyView, R.id.empty_tips_show, mViewSetter.getEmpty().content);
            BaseVaryViewSetter.setImageSrc(mEmptyView, R.id.empty_img, mViewSetter.getEmpty().imageId);
        }
        if (mErrorView != null) {
            BaseVaryViewSetter.setText(mErrorView, R.id.error_tips_show, mViewSetter.getError().content);
            BaseVaryViewSetter.setImageSrc(mErrorView, R.id.error_img, mViewSetter.getError().imageId);
        }
        mViewHelper.setVaryViewBackground(mViewSetter.getVaryViewBackground());
        mViewHelper.setViewAnimProvider(mViewSetter.getViewAnimProvider());
    }

    public static class Builder {
        private View mErrorView;
        private View mLoadingView;
        private View mEmptyView;
        private View mDataView;
        private View.OnClickListener mRefreshListener;

        public Builder setErrorView(View errorView) {
            mErrorView = errorView;
            return this;
        }

        public Builder setLoadingView(View loadingView) {
            mLoadingView = loadingView;
            return this;
        }

        public Builder setEmptyView(View emptyView) {
            mEmptyView = emptyView;
            return this;
        }

        public Builder setDataView(View dataView) {
            mDataView = dataView;
            return this;
        }

        public Builder setRefreshListener(View.OnClickListener refreshListener) {
            mRefreshListener = refreshListener;
            return this;
        }

        public VaryViewHelper build() {
            VaryViewHelper helper = new VaryViewHelper(mDataView);
            if (mEmptyView != null) {
                helper.setUpEmptyView(mEmptyView);
            }
            if (mErrorView != null) {
                helper.setUpErrorView(mErrorView, mRefreshListener);
            }
            if (mLoadingView != null) {
                helper.setUpLoadingView(mLoadingView);
            }
            return helper;
        }
    }

}
