package worldgo.common.viewmodel.refresh;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import worldgo.common.R;
import worldgo.common.viewmodel.framework.base.view.BaseRefreshView;
import worldgo.common.viewmodel.refresh.util.CustomLoadMoreView;

public class RefreshListView extends LinearLayout {
    public final static int Refresh = 0;
    public final static int LoadMore = 1;
    public final static int Refresh_LoadMore = 2;
    public final static int DEFAULT_SIZE = 10;
    private final Handler mHandler;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private IRefreshListener mIRefreshListener;
    private Context mContext;
    private BaseQuickAdapter mAdapter;
    private boolean isLoadMore;//是否允许加载更多
    private int size = DEFAULT_SIZE;//每次加载条目
    private int totalPage = -1;//加载总页数
    private int pageStartOffset = 0;//起始页
    private int currentPage = pageStartOffset;//当前加载页
    @Type
    private int mRefreshType;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mHandler = new Handler(Looper.getMainLooper());
        View content = View.inflate(context, R.layout.view_refresh_layout, null);
        addView(content);

        mRefreshLayout = (RefreshLayout) content.findViewById(R.id.mRefreshLayout);
        mRecyclerView = (RecyclerView) content.findViewById(R.id.mRecycleView);

        mContext = context;

        initAttr(context, attrs);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLogic(true);
    }

    private void initLogic(boolean init) {
        switch (mRefreshType) {
            case Refresh:
                if (init) {
                    requireRefresh();
                }
                break;
            case LoadMore:
                requireLoadMore();
                break;
            case Refresh_LoadMore:
                if (init) {
                    requireRefresh();
                }
                requireLoadMore();
                break;
        }
    }

    private void requireLoadMore() {
        if (mAdapter != null) {
            isLoadMore = true;
            mAdapter.setLoadMoreView(new CustomLoadMoreView());//customer loadView
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (mIRefreshListener != null
                            && mRefreshLayout.getCurrentRefreshStatus() == RefreshLayout.RefreshStatus.IDLE) {
                        if (isLoadMore) {
                            mIRefreshListener.onLoadMore(currentPage + 1);
                        } else {
                            mAdapter.loadMoreEnd();
                        }
                    }else{
                        mAdapter.loadMoreComplete();
                    }
                }
            }, mRecyclerView);
        }

    }

    private void requireRefresh() {
        mRefreshLayout.setRefreshViewHolder(new NormalRefreshViewHolder(mContext, false));
        mRefreshLayout.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                if (mIRefreshListener != null) {
                    mIRefreshListener.onRefresh(refreshLayout);
                }
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                return false;
            }
        });
    }


    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.refreshLayout);
        //type

        mRefreshType = ta.getInt(R.styleable.refreshLayout_rl_refresh_type, Refresh);
        //...
        ta.recycle();
    }

    private void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    /**
     * ---------------------------------------------------------------------------------------------
     */

    /**
     * 加载总页数，通常由接口获取，若没有可传0
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;

    }

    public int getSize() {
        return size;
    }

    /**
     * 每次加载条目数，与保持接口一致!
     */
    public RefreshListView setSize(int size) {
        this.size = size;
        return this;
    }

    public int getPageStartOffset() {
        return pageStartOffset;
    }

    public RefreshListView setPageStartOffset(int start) {
        this.pageStartOffset = start;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setRefreshListener(IRefreshListener mIRefreshListener) {
        this.mIRefreshListener = mIRefreshListener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public RefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public void setAdapter(BaseQuickAdapter mAdapter) {
        this.mAdapter = mAdapter;
        initLogic(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 刷新View的类型
     *
     * @param type
     */
    public void setRefreshType(@Type int type) {
        this.mRefreshType = type;

    }

    public void setData(List beanList, boolean loadMore) {
        if (beanList == null || beanList.size() == 0) return;
        //refresh
        if (!loadMore) {
            mAdapter.setNewData(beanList);
            currentPage = pageStartOffset;
            mRefreshLayout.endRefreshing();
            setLoadMore(true);
        } else {
            //没有传递totalPage，（验证发生在下次加载时）
            if (totalPage == -1 || totalPage == 0) {
                boolean valid = beanList.size() >= size;

                if (valid) {
                    setLoadMore(true);
                    mAdapter.addData(beanList);
                    currentPage++;
                    mAdapter.loadMoreComplete();
                } else {
                    setLoadMore(false);
                    mAdapter.addData(beanList);
                    currentPage++;
                    mAdapter.loadMoreEnd();
                }
                return;
            }
            //有传递totalPage，（验证发生在这次加载后）
            if (currentPage < (totalPage + pageStartOffset) - 1) {
                mAdapter.addData(beanList);
                mAdapter.loadMoreComplete();
                currentPage++;
                setLoadMore(currentPage < (totalPage + pageStartOffset) - 1);
            } else {
                setLoadMore(false);
                mAdapter.loadMoreEnd();
            }
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------
     */

    public static interface IRefreshListener {
        void onRefresh(RefreshLayout refreshLayout);

        void onLoadMore(int loadPage);
    }

    @IntDef(value = {Refresh, LoadMore, Refresh_LoadMore})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    public static interface IRefreshView<BEAN> extends BaseRefreshView<BEAN> {

    }
}
