package worldgo.rad.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.jaeger.library.StatusBarUtil;

import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.common.viewmodel.util.view.DragPhotoView;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingActivity;
import worldgo.rad.databinding.AcitivtyDragPhotoBinding;
import worldgo.rad.vm.DragDetailVM;

/**
 * @author ricky.yao on 2017/3/30.
 */

public class DragPhotoActivity extends BaseBindingActivity<IView, DragDetailVM, AcitivtyDragPhotoBinding> {
    int mOriginLeft;
    int mOriginTop;
    int mOriginHeight;
    int mOriginWidth;
    int mOriginCenterX;
    int mOriginCenterY;
    private float mTargetHeight;
    private float mTargetWidth;
    private float mScaleX;
    private float mScaleY;
    private float mTranslationX;
    private float mTranslationY;
    private boolean isAnim;

    public static void startPhotoActivity(Activity context, View imageView, String url,boolean isCrop) {
        if (StringUtils.isEmpty(url)) return;
        Intent intent = new Intent(context, DragPhotoActivity.class);


        if(imageView!=null){
            int location[] = new int[2];
            imageView.getLocationOnScreen(location);
            intent.putExtra("left", location[0]);
            intent.putExtra("top", location[1]);
            intent.putExtra("height", imageView.getHeight());
            intent.putExtra("width", imageView.getWidth());
            intent.putExtra("isAnim", true);
        }

        intent.putExtra("url", url);
        intent.putExtra("isCrop", isCrop);

        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {


        if (getViewModel().portrait == -1) {
            getViewModel().portrait = ScreenUtils.isPortrait() ? 1 : 2;
        }
        StatusBarUtil.setTransparent(this);

        mBinding.mDragView.setScaleType(getIntent().getBooleanExtra("isCrop",false)? ImageView.ScaleType.CENTER_CROP : ImageView.ScaleType.FIT_CENTER);
        CommonUtils.imageLoad( mBinding.mDragView, getIntent().getStringExtra("url"));

        isAnim = getIntent().getBooleanExtra("isAnim",false);

        mBinding.mDragView.setOnTapListener(new DragPhotoView.OnTapListener() {
            @Override
            public void onTap(DragPhotoView view) {
                finishWithAnimation();
            }
        });

        mBinding.mDragView.setOnExitListener(new DragPhotoView.OnExitListener() {
            @Override
            public void onExit(DragPhotoView view, float x, float y, float w, float h) {
                performExitAnimation(view, x, y, w, h);
            }
        });
        if(isAnim){
            mBinding.mDragView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mBinding.mDragView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            mOriginLeft = getIntent().getIntExtra("left", 0);
                            mOriginTop = getIntent().getIntExtra("top", 0);
                            mOriginHeight = getIntent().getIntExtra("height", 0);
                            mOriginWidth = getIntent().getIntExtra("width", 0);
                            mOriginCenterX = mOriginLeft + mOriginWidth / 2;
                            mOriginCenterY = mOriginTop + mOriginHeight / 2;

                            int[] location = new int[2];

                            final DragPhotoView photoView = mBinding.mDragView;
                            photoView.getLocationOnScreen(location);

                            mTargetHeight = (float) photoView.getHeight();
                            mTargetWidth = (float) photoView.getWidth();
                            mScaleX = (float) mOriginWidth / mTargetWidth;
                            mScaleY = (float) mOriginHeight / mTargetHeight;

                            float targetCenterX = location[0] + mTargetWidth / 2;
                            float targetCenterY = location[1] + mTargetHeight / 2;

                            mTranslationX = mOriginCenterX - targetCenterX;
                            mTranslationY = mOriginCenterY - targetCenterY;
                            photoView.setTranslationX(mTranslationX);
                            photoView.setTranslationY(mTranslationY);

                            photoView.setScaleX(mScaleX);
                            photoView.setScaleY(mScaleY);

                            performEnterAnimation();

                            mBinding.mDragView.setMinScale(mScaleX);
                        }
                    });
        }

    }

    private void finishWithAnimation() {
        final DragPhotoView photoView = mBinding.mDragView;

        int t = ScreenUtils.isPortrait() ? 1 : 2;
        if (!isAnim || getViewModel().portrait != t) {
            //进来时和出去时屏幕方向不一致
            ValueAnimator v = ValueAnimator.ofFloat(1, 0);
            v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    photoView.setAlpha((Float) valueAnimator.getAnimatedValue());
                }
            });
            v.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animation.removeAllListeners();
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
            v.setDuration(200);
            v.start();
            return;
        }


        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(0, mTranslationX);
        translateXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateXAnimator.setDuration(200);
        translateXAnimator.start();

        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(0, mTranslationY);
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateYAnimator.setDuration(200);
        translateYAnimator.start();

        ValueAnimator scaleYAnimator = ValueAnimator.ofFloat(1, mScaleY);
        scaleYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setScaleY((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleYAnimator.setDuration(200);
        scaleYAnimator.start();

        ValueAnimator scaleXAnimator = ValueAnimator.ofFloat(1, mScaleX);
        scaleXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        scaleXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeAllListeners();
                finish();
                overridePendingTransition(0, 0);
            }
        });
        scaleXAnimator.setDuration(200);
        scaleXAnimator.start();
    }

    private void performEnterAnimation() {
        final DragPhotoView photoView = mBinding.mDragView;
        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(photoView.getX(), 0);
        translateXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateXAnimator.setDuration(300);
        translateXAnimator.start();

        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(photoView.getY(), 0);
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateYAnimator.setDuration(300);
        translateYAnimator.start();

        ValueAnimator scaleYAnimator = ValueAnimator.ofFloat(mScaleY, 1);
        scaleYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setScaleY((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleYAnimator.setDuration(300);
        scaleYAnimator.start();

        ValueAnimator scaleXAnimator = ValueAnimator.ofFloat(mScaleX, 1);
        scaleXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                photoView.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleXAnimator.setDuration(300);
        scaleXAnimator.start();
    }

    /**
     * ===================================================================================
     * <p>
     * 底下是低版本"共享元素"实现   不需要过分关心  如有需要 可作为参考.
     * <p>
     * Code  under is shared transitions in all android versions implementation
     */
    private void performExitAnimation(final DragPhotoView view, float x, float y, float w, float h) {
        view.finishAnimationCallBack();
        float viewX = mTargetWidth / 2 + x - mTargetWidth * mScaleX / 2;
        float viewY = mTargetHeight / 2 + y - mTargetHeight * mScaleY / 2;
        view.setX(viewX);
        view.setY(viewY);

        float centerX = view.getX() + mOriginWidth / 2;
        float centerY = view.getY() + mOriginHeight / 2;

        float translateX = mOriginCenterX - centerX;
        float translateY = mOriginCenterY - centerY;


        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(view.getX(), view.getX() + translateX);
        translateXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateXAnimator.setDuration(300);
        translateXAnimator.start();
        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(view.getY(), view.getY() + translateY);
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateYAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        translateYAnimator.setDuration(300);
        translateYAnimator.start();
    }


    @Override
    public void onBackPressed() {
        finishWithAnimation();
    }

    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return new ViewModelBindingConfig(R.layout.acitivty_drag_photo, mContext);
    }
}
