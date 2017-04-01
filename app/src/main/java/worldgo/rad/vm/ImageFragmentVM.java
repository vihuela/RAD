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
import worldgo.common.viewmodel.refresh.RefreshListView;
import worldgo.common.viewmodel.refresh.interfaces.IRefreshView;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.request.call.JsonCallback;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.ui.ImageFragment;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class ImageFragmentVM extends AbsVM<IRefreshView> implements RefreshListView.IRestoreInstance {

    //双向绑定
    public final ObservableField<String> title = new ObservableField<>();

    private RefreshListView.SaveInstance mOnSaveInstance;


    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull IRefreshView view) {
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

                //获取总页数
                int totalPage = 20;

                getViewOptional().getRefreshView().setTotalPage(totalPage);
                getViewOptional().getRefreshView().setData(res.results, loadMore);
            }

            @Override
            public void error(Error error, String message) {
                super.error(error, message);
                getViewOptional().getRefreshView().setMessage(error, message);
            }
        }, iNetQueue);
    }

    @Override
    public boolean isLoaded() {
        return mOnSaveInstance !=null && mOnSaveInstance.isLoaded();
    }

    @Override
    public void setSaveInstance(@NonNull RefreshListView.SaveInstance saveInstance) {
        mOnSaveInstance = saveInstance;
    }

    @Override
    public RefreshListView.SaveInstance getSaveInstance() {
        return mOnSaveInstance;
    }

}
