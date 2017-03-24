package worldgo.rad.vm;

import android.Manifest;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;

import worldgo.common.viewmodel.aop.anno.CheckLogin;
import worldgo.common.viewmodel.aop.anno.Permission;
import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.ui.PagerItemFragment;
import worldgo.rad.viewImpl.IPagerItemView;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class PagerItemVM extends AbstractViewModel<IPagerItemView> {
    //双向绑定
    public final ObservableField<String> title = new ObservableField<>();

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull IPagerItemView view) {
        super.onBindView(view);
            if (view instanceof PagerItemFragment) {
                PagerItemFragment pagerItemFragment = (PagerItemFragment) view;
                title.set(pagerItemFragment.mTitle);

                FragmentPagerItemBinding binding = pagerItemFragment.getBinding();
                CommonUtils.singleClick(binding.btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                    }
                });
            }

    }
    @Permission(Manifest.permission.CAMERA)
    public void takePhoto() {
        ToastUtils.showShortToast("启动相机");
    }

    @CheckLogin
    public void loginCheck(){

    }
}
