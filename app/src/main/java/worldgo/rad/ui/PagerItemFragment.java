package worldgo.rad.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.common.viewmodel.util.decoration.GridItemDecoration;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingFragment;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.request.Api;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.vm.PagerItemVM;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class PagerItemFragment extends BaseBindingFragment<RefreshListView.IRefreshView, PagerItemVM, FragmentPagerItemBinding> implements RefreshListView.IRefreshView<ImageListRequest.Res.Item> {
    private static List mHeights = new ArrayList();

    static {
        Collections.addAll(mHeights, SizeUtils.dp2px(250), SizeUtils.dp2px(100), SizeUtils.dp2px(150));
    }

    public String mTitle;

    public static PagerItemFragment getInstance(String title) {
        PagerItemFragment sf = new PagerItemFragment();
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
        return new ViewModelBindingConfig(R.layout.fragment_pager_item, mContext);
    }

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString("title");

        mBinding.mRefreshLayout.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mBinding.mRefreshLayout.getRecyclerView().addItemDecoration(new GridItemDecoration(2, SizeUtils.dp2px(5), false));
        BaseQuickAdapter<ImageListRequest.Res.Item, BaseViewHolder> mAdapter = new BaseQuickAdapter<ImageListRequest.Res.Item, BaseViewHolder>(R.layout.image_item) {


            @Override
            protected void convert(BaseViewHolder helper, ImageListRequest.Res.Item item) {


                ViewGroup.LayoutParams lp = helper.getView(R.id.iv).getLayoutParams();
                lp.height = (int) mHeights.get(helper.getAdapterPosition() % mHeights.size());

                helper.getView(R.id.iv).setLayoutParams(lp);

                CommonUtils.imageLoad((ImageView) helper.getView(R.id.iv), item.url);
            }
        };
        mBinding.mRefreshLayout
                .setBaseView(this)
                .setPageStartOffset(0)
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
        showLoading();
        onRefreshData();
    }


    @Override
    public void setTotalPage(int totalPage) {
        mBinding.mRefreshLayout.setPageTotal(totalPage);
    }

    @Override
    public void setData(List<ImageListRequest.Res.Item> items, boolean loadMore) {
        mBinding.mRefreshLayout.setData(items, loadMore);
    }

    @Override
    public void setMessage(Error error, String content) {
        mBinding.mRefreshLayout.setMessage(error, content);
    }

    @Override
    public void onRefreshData() {
        getViewModel().getImageList(mBinding.mRefreshLayout.getSize(), mBinding.mRefreshLayout.getPageStartOffset(), mNetQueue, false);
    }
}
