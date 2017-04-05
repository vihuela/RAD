package worldgo.rad.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.utils.HandlerUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.refresh.RefreshLayout;
import worldgo.common.viewmodel.refresh.RefreshListView;
import worldgo.common.viewmodel.refresh.base.BaseRefreshView;
import worldgo.common.viewmodel.refresh.interfaces.IRefreshView;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.common.viewmodel.util.decoration.GridItemDecoration;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingFragment;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.request.Api;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.vm.ImageFragmentVM;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class ImageFragment extends BaseBindingFragment<IRefreshView, ImageFragmentVM, FragmentPagerItemBinding> implements IRefreshView{
    private static List mHeights = new ArrayList();

    static {
        Collections.addAll(mHeights, ScreenUtils.getScreenHeight() / 2, ScreenUtils.getScreenHeight() / 2 / 2);
    }

    public String mTitle;

    public static ImageFragment getInstance(String title) {
        ImageFragment sf = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        sf.setArguments(args);
        return sf;
    }

    @Override
    protected View setStatusTargetView() {
        return mBinding.mRefreshLayout;
    }

    @Override
    public void setOnRetryListener() {
        onRefreshData();
    }

    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return new ViewModelBindingConfig(R.layout.fragment_pager_item, getActivity());
    }

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString("title");

        mBinding.mRefreshLayout.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mBinding.mRefreshLayout.getRecyclerView().addItemDecoration(new GridItemDecoration(2, SizeUtils.dp2px(5), false));
        BaseQuickAdapter<ImageListRequest.Res.Item, BaseViewHolder> mAdapter = new BaseQuickAdapter<ImageListRequest.Res.Item, BaseViewHolder>(R.layout.image_item) {


            @Override
            protected void convert(BaseViewHolder helper, ImageListRequest.Res.Item item) {

                final ImageView iv = helper.getView(R.id.iv);

                CommonUtils.imageLoad(getActivity(),iv, item.url, ImageView.ScaleType.CENTER_CROP);

//                helper.addOnClickListener(R.id.iv)
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DragPhotoActivity.startPhotoActivity(getActivity(), view, ((ImageListRequest.Res.Item) adapter.getItem(position)).url);
            }
        });

        mBinding.mRefreshLayout
                .setBaseView(this)
                .setRestoreView(getViewModel())
                .setPageStartOffset(1)
                .setPageSize(Api.LIST_SIZE)
                .setViewType(RefreshListView.Refresh_LoadMore)
                .setListener(new RefreshListView.IRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        onRefreshData();
                    }

                    @Override
                    public void onLoadMore(int targetPage) {
                        getViewModel().getImageList(mBinding.mRefreshLayout.getSize(), targetPage, mNetQueue, true);
                    }
                })
                .setAdapter(mAdapter);
    }


    @Override
    protected void onFirstUserVisible() {
        if (!getViewModel().isLoaded()) {
            showLoading();
            onRefreshData();
        }
    }


    @Override
    public BaseRefreshView getRefreshView() {
        return mBinding.mRefreshLayout;
    }


    public void onRefreshData() {
        getViewModel().getImageList(mBinding.mRefreshLayout.getSize(), mBinding.mRefreshLayout.getPageStartOffset(), mNetQueue, false);
    }

}
