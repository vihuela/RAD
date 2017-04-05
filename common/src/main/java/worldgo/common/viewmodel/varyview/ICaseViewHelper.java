/*
 * Copyright (c) 2016. WorldGo Technology Co., Ltd
 * DO NOT DIVULGE
 */

package worldgo.common.viewmodel.varyview;

import android.content.Context;
import android.view.View;

import worldgo.common.viewmodel.varyview.anim.VaryViewAnimProvider;

/**
 * 功能：切换页面的接口
 */
public interface ICaseViewHelper {

    /**
     * <p>获取上下文</p>
     *
     * @return Context
     */
    Context getContext();

    /**
     * <p>获取显示数据的View</p>
     *
     * @return View
     */
    View getDataView();

    /**
     * <p>获取当前正在显示的View</p>
     *
     * @return View
     */
    View getCurrentView();

    /**
     * <p>切换View</p>
     *
     * @param view 需要显示的View
     */
    void showCaseLayout(View view);

    /**
     * <p>切换View</p>
     *
     * @param layoutId 需要显示布局id
     */
    void showCaseLayout(int layoutId);

    /**
     * <p>恢复显示数据的View</p>
     */
    void restoreLayout();

    /**
     * <p>实例化布局</p>
     *
     * @param layoutId 需要实例化的布局id
     * @return View
     */
    View inflate(int layoutId);

    /**
     * <p>切换View时的动画</p>
     *
     * @param viewAnimProvider 动画提供
     */
    void setViewAnimProvider(VaryViewAnimProvider viewAnimProvider);
}
