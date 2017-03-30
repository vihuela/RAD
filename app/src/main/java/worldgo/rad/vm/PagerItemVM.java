package worldgo.rad.vm;

import android.Manifest;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.ToastUtils;

import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.aop.anno.Permission;
import worldgo.common.viewmodel.framework.base.view.BaseRefreshView;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.request.call.JsonCallback;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.ui.PagerItemFragment;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class PagerItemVM extends AbsVM<BaseRefreshView> {
    //双向绑定
    public final ObservableField<String> title = new ObservableField<>();

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull BaseRefreshView view) {
        super.onBindView(view);
        if (view instanceof PagerItemFragment) {
            PagerItemFragment pagerItemFragment = (PagerItemFragment) view;
            title.set(pagerItemFragment.mTitle);

            FragmentPagerItemBinding binding = pagerItemFragment.getBinding();
//            CommonUtils.singleClick(binding.btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    takePhoto();
//                }
//            });
        }

    }

    @Permission(Manifest.permission.CAMERA)
    public void takePhoto() {
        ToastUtils.showShortToast("启动相机");
    }

    public void getImageList(int size, int page, INetQueue iNetQueue, final boolean loadMore) {
        mApi.getImageList(size, page).execute(new JsonCallback<ImageListRequest.Res>() {
            @Override
            public void success(ImageListRequest.Res res, boolean fromCache) {
                //获取总页数
                if (getView() != null) {
                    getView().setTotalPage(30);
                    getView().setData(res.results, loadMore);
                }
            }

            @Override
            public void error(Error error, String message) {
                super.error(error, message);
                getView().setMessage(error, message);
            }
        }, iNetQueue);
    }

}
