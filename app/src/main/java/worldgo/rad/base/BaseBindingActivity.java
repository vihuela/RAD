package worldgo.rad.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.lifecycle.NetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBaseBindingActivity;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.varyview.VaryViewHelper;
import worldgo.rad.R;
import worldgo.rad.util.DefaultVaryViewSetter;

/**
 * @author ricky.yao on 2017/3/23.
 *         xml新增组件refresh All Gradle projects即可识别
 */

public abstract class BaseBindingActivity<T extends IView, R1 extends AbstractViewModel<T>, B extends ViewDataBinding> extends ViewModelBaseBindingActivity<T, R1, B> implements BGASwipeBackHelper.Delegate {


    //net queue
    public INetQueue mNetQueue;
    protected B mBinding;
    protected Context mContext;
    //swipeBack
    protected BGASwipeBackHelper mSwipeBackHelper;
    private VaryViewHelper mVaryViewHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (isSupportSwipeBack()) {
            initSwipeBackFinish();
        }
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = getBinding();
        mNetQueue = new NetQueue();

        setupVaryView();
        setModelView((T) this);
        onCreateView(savedInstanceState);

    }

    private void setupVaryView() {
        if (getStatusTargetView() != null) {//无效请给getStatusTargetView()套一层FrameLayout
            mVaryViewHelper = new VaryViewHelper.Builder()
                    .setDataView(getStatusTargetView())
                    .setLoadingView(LayoutInflater.from(mContext).inflate(worldgo.common.R.layout.vary_view_loadingview, null))
                    .setEmptyView(LayoutInflater.from(mContext).inflate(worldgo.common.R.layout.vary_view_emptyview, null))
                    .setErrorView(LayoutInflater.from(mContext).inflate(worldgo.common.R.layout.vary_view_errorview, null))
                    .setRefreshListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setOnRetryListener();
                        }
                    })
                    .build();
            mVaryViewHelper.setViewSetter(new DefaultVaryViewSetter());
        }
    }

    /**
     * 切换状态（可选覆盖）
     */
    protected View getStatusTargetView() {
        return null;
    }

    /**
     * VaryView中点击重试回调（可选覆盖）
     */
    protected void setOnRetryListener() {

    }

    public abstract void onCreateView(@Nullable Bundle savedInstanceState);

    public abstract ViewModelBindingConfig getViewModelBindingConfig();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVaryViewHelper != null) mVaryViewHelper.releaseVaryView();
        mNetQueue.cancel();
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);//不能与inflateMenu同用，写了这句就使用onCreateOptionsMenu、onOptionsItemSelected处理菜单相关
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void showEmpty() {
        if (mVaryViewHelper != null) mVaryViewHelper.showEmptyView();
    }

    @Override
    public void showLoading() {
        if (mVaryViewHelper != null) mVaryViewHelper.showLoadingView();
    }

    @Override
    public void showNetError(Error error, String content) {
        //error can be use
        if (mVaryViewHelper != null) mVaryViewHelper.showErrorView(content);
    }

    @Override
    public void showMessage(String content) {
        ToastUtils.showShortToast(content);
    }

    @Override
    public void showContent() {

        if (mVaryViewHelper != null) mVaryViewHelper.showDataView();
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
