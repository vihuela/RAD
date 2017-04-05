package worldgo.rad.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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

import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.refresh.RefreshLayout;
import worldgo.common.viewmodel.refresh.RefreshListView;
import worldgo.common.viewmodel.refresh.base.BaseRefreshView;
import worldgo.common.viewmodel.refresh.interfaces.IRefreshView;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.common.viewmodel.util.decoration.CommonItemDecoration;
import worldgo.common.viewmodel.util.decoration.GridItemDecoration;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingFragment;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.databinding.FragmentPagerItemNewBinding;
import worldgo.rad.request.Api;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.request.entity.NewsRequest;
import worldgo.rad.vm.ImageFragmentVM;
import worldgo.rad.vm.NewsFragmentVM;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class NewsFragment extends BaseBindingFragment<IRefreshView, NewsFragmentVM, FragmentPagerItemNewBinding> implements IRefreshView {

    public String mTitle;

    public static NewsFragment getInstance(String title) {
        NewsFragment sf = new NewsFragment();
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
        return new ViewModelBindingConfig(R.layout.fragment_pager_item_new, getActivity());
    }

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString("title");

        mBinding.mRefreshLayout.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        BaseQuickAdapter<NewsRequest.Res.StoriesBean, BaseViewHolder> mAdapter = new BaseQuickAdapter<NewsRequest.Res.StoriesBean  , BaseViewHolder>(R.layout.item_nomal_story) {


            @Override
            protected void convert(BaseViewHolder helper, NewsRequest.Res.StoriesBean  item) {

                if(item.images!=null && item.images.size()>0){

                    final ImageView iv = helper.getView(R.id.image_view);
                    CommonUtils.imageLoad(iv, item.images.get(0), ImageView.ScaleType.CENTER_CROP);
                }


                helper.setText(R.id.tv_title,item.title);

            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        mBinding.mRefreshLayout
                .setBaseView(this)
                .setRestoreView(getViewModel())
                .setPageStartOffset(0)
                .setPageSize(1)
                .setViewType(RefreshListView.Refresh_LoadMore)
                .setListener(new RefreshListView.IRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        onRefreshData();
                    }

                    @Override
                    public void onLoadMore(int targetPage) {
                        getViewModel().getNewForDate(targetPage,mNetQueue);
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
        getViewModel().getNewForLast(mNetQueue);
    }

}
