package worldgo.rad.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;

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

public abstract class BaseBindingActivity<T extends IView, R1 extends AbstractViewModel<T>, B extends ViewDataBinding> extends ViewModelBaseBindingActivity<T, R1, B> {


    //net queue
    public INetQueue mNetQueue;
    protected B mBinding;
    protected Context mContext;
    private TextView title;
    private ImageView back;
    private VaryViewHelper mVaryViewHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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

//    @Override
//    public void setContentView(int layoutId) {
//        setContentView(View.inflate(this, layoutId, null));
//    }
//
//    @Override
//    public void setContentView(View view) {
//        View contain = View.inflate(this,R.layout.activity_base,null);
//        LinearLayout rootLayout = (LinearLayout) contain.findViewById(R.id.root_layout);
//        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        initToolbar();
//    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    public void showEmpty() {
        if (mVaryViewHelper != null)mVaryViewHelper.showEmptyView();
    }

    @Override
    public void showLoading() {
        if (mVaryViewHelper != null)mVaryViewHelper.showLoadingView();
    }

    @Override
    public void showNetError(Error error, String content) {
        //error can be use
        if (mVaryViewHelper != null)mVaryViewHelper.showErrorView(content);
    }

    @Override
    public void showMessage(String content) {
        ToastUtils.showShortToast(content);
    }

    @Override
    public void showContent() {
        if (mVaryViewHelper != null)mVaryViewHelper.showDataView();
    }
}
