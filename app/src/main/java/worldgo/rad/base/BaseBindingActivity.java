package worldgo.rad.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.lifecycle.NetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBaseBindingActivity;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.rad.R;

/**
 * @author ricky.yao on 2017/3/23.
 * xml新增组件refresh All Gradle projects即可识别
 */

public abstract class BaseBindingActivity<T extends IView, R1 extends AbstractViewModel<T>, B extends ViewDataBinding> extends ViewModelBaseBindingActivity<T, R1, B> {


    //net queue
    public INetQueue mNetQueue;
    protected B mBinding;
    protected Context mContext;
    private TextView title;
    private ImageView back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = getBinding();
        mNetQueue = new NetQueue();

        onCreateView(savedInstanceState);
        setModelView((T) this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
    }

    public abstract void onCreateView(@Nullable Bundle savedInstanceState);

    public abstract ViewModelBindingConfig getViewModelBindingConfig();

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void showNetError(Error error, String content) {

    }

    @Override
    public void showMessage(String content) {

    }
}
