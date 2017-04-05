/*
 * Copyright (c) 2016. WorldGo Technology Co., Ltd
 * DO NOT DIVULGE
 */

package worldgo.common.viewmodel.varyview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import worldgo.common.R;
import worldgo.common.viewmodel.varyview.anim.VaryViewAnimProvider;

/**
 * 功能：切换布局，用一个新的View替换原先的View
 */
public class ReplaceViewHelper implements ICaseViewHelper {

    public View mDataView;
    public View mCurrentView;

    public ViewGroup mParentView;
    public ViewGroup.LayoutParams mLayoutParams;
    public int mViewIndex;
    private VaryViewAnimProvider mViewAnimProvider;

    public ReplaceViewHelper(View dataView) {

        /*记录显示数据的View*/
        this.mDataView = dataView;

        mLayoutParams = dataView.getLayoutParams();

        /*记录父View*/
        if (dataView.getParent() != null) {
            mParentView = (ViewGroup) dataView.getParent();
        } else {
            mParentView = (ViewGroup) dataView.getRootView();
        }

        /*记录要显示的View在父View中的位置*/
        int childCount = mParentView.getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (dataView == mParentView.getChildAt(index)) {
                mViewIndex = index;
                break;
            }
        }

        this.mCurrentView = dataView;
    }


    @Override
    public Context getContext() {
        return mDataView.getContext();
    }

    @Override
    public View getDataView() {
        return mDataView;
    }

    @Override
    public View getCurrentView() {
        return mCurrentView;
    }

    @Override
    public void restoreLayout() {
        showCaseLayout(mDataView);
    }

    /**
     * toView 在holderView与targetView之间切换
     */
    @Override
    public void showCaseLayout(final View toView) {
        if (toView == null) {
            return;
        }
        this.mCurrentView = toView;
        /*如果要显示的View跟已显示View一样，就不用切换了*/
        final View fromView = mParentView.getChildAt(mViewIndex);
        if (fromView != toView) {
            ViewGroup parent = (ViewGroup) toView.getParent();
            if (parent != null) {
                parent.removeView(toView);
            }

            //remove anim
            ValueAnimator hideAnimation = mViewAnimProvider.hideAnimation();
            if (hideAnimation != null && toView.getId() == R.id.varyView_holder_view) {
                //还原布局
                //fromView
                fromView.setAlpha(1);

                hideAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        fromView.setAlpha((Float) animation.getAnimatedValue());
                    }
                });
                hideAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.removeAllListeners();
                        removeFromViewAddToView(toView);
                        fromView.setAlpha(1);
                    }
                });

                hideAnimation.start();
            } else {
                removeFromViewAddToView(toView);
            }


        }
    }

    private void removeFromViewAddToView(View toView) {
        //在同一个位置替换
        mParentView.removeViewAt(mViewIndex);
        mParentView.addView(toView, mViewIndex, mLayoutParams);
    }

    @Override
    public void showCaseLayout(int layoutId) {
        showCaseLayout(inflate(layoutId));
    }

    @Override
    public View inflate(int layoutId) {
        return LayoutInflater.from(mDataView.getContext()).inflate(layoutId, null);
    }

    @Override
    public void setViewAnimProvider(VaryViewAnimProvider viewAnimProvider) {
        mViewAnimProvider = viewAnimProvider;
    }

    private class AniLis implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
