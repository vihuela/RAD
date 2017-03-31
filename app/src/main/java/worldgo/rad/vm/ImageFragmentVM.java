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
import worldgo.common.viewmodel.refresh.RefreshListView;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.request.call.JsonCallback;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.ui.ImageFragment;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class ImageFragmentVM extends AbsVM<BaseRefreshView> implements RefreshListView.IRestoreView{
    //双向绑定
    public final ObservableField<String> title = new ObservableField<>();

    private RefreshListView.SaveInstance mSaveInstance;


    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull BaseRefreshView view) {
        super.onBindView(view);
        if (view instanceof ImageFragment) {
            ImageFragment imageFragment = (ImageFragment) view;
            title.set(imageFragment.mTitle);

            FragmentPagerItemBinding binding = imageFragment.getBinding();
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

                if (getView() != null) {
                    //获取总页数
                    int totalPage = 20;

                    getView().setTotalPage(totalPage);
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

    @Override
    public boolean isDataView() {
        return mSaveInstance!=null && mSaveInstance.isDataView();
    }

    @Override
    public void setSaveInstance(RefreshListView.SaveInstance saveInstance) {
        mSaveInstance = saveInstance;
    }

    @Override
    public RefreshListView.SaveInstance getSaveInstance() {
        return mSaveInstance;
    }
}
