package worldgo.rad.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.common.viewmodel.util.ViewUtils;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingActivity;
import worldgo.rad.databinding.ActivityDetailBinding;
import worldgo.rad.request.entity.NewsDetailRequest;
import worldgo.rad.vm.NewDetailVM;

/**
 * @author ricky.yao on 2017/4/6.
 */

public class NewsDetailActivity extends BaseBindingActivity<IView, NewDetailVM, ActivityDetailBinding> {
    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {

        StatusBarUtil.setTransparentForImageViewInFragment(this, null);

        setupToolbar();

        getViewModel().initWV(mBinding.webView);

        showLoading();
        getViewModel().getNesDetail(mNetQueue);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        View contentView = View.inflate(mContext, R.layout.dialog_detial, null);

        ViewUtils.apply(ViewUtils.generateViews(contentView, R.id.layout_open_in_browser, R.id.layout_copy_text), new ViewUtils.Action<View>() {
            @Override
            public void apply(@NonNull View view, final int index) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (index) {
                            case 0:
                                getViewModel().openBroswer();
                                break;
                            case 1:
                                getViewModel().copyBodyContent();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.setContentView(contentView);
        dialog.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected View getStatusTargetView() {
        return mBinding.coordinatorLayout;
    }

    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return new ViewModelBindingConfig(R.layout.activity_detail, this);
    }

    public void setData(NewsDetailRequest.Res data) {
        showContent();
        //title
        mBinding.toolbarLayout.setTitle(data.title);
        //image
        CommonUtils.imageLoad(this, mBinding.imageView, data.image, ImageView.ScaleType.CENTER_CROP);
        //content
        if (data.body == null) {
            mBinding.webView.loadUrl(data.share_url);
        } else {
            mBinding.webView.loadDataWithBaseURL("x-data://base", getViewModel().convertBody(data.body), "text/html", "utf-8", null);
        }
    }


}
