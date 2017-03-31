package worldgo.common.viewmodel.refresh;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
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

import ricky.oknet.utils.Error;
import worldgo.common.R;
import worldgo.common.viewmodel.framework.base.view.BaseRefreshView;
import worldgo.common.viewmodel.framework.base.view.BaseRefreshViewAdapter;
import worldgo.common.viewmodel.framework.base.view.BaseView;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.common.viewmodel.refresh.util.CustomLoadMoreView;

public class RefreshListView<BEAN> extends LinearLayout implements BaseRefreshView<BEAN> {
    public final static int DEFAULT_SIZE = 10;
    public final static int Refresh = 0;
    public final static int LoadMore = 1;
    public final static int Refresh_LoadMore = 2;
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
    private BaseView mBaseView;//与外部对接View的切换
    private IRestoreView mRestoreView;
    private int mRefreshType = Refresh;
    //onSave
    private int mOnSaveInstance_totalPage;
    private boolean mOnSaveInstance_loadMore;
    private Error mOnSaveInstance_error;
    private String mOnSaveInstance_content;

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

    private void initLogic() {
        switch (mRefreshType) {
            case Refresh:
                requireRefresh();
                break;
            case LoadMore:
                requireLoadMore();
                break;
            case Refresh_LoadMore:
                requireRefresh();
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
                    } else {
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
     * --------------------------------------Setter----------------------------------------------------
     */

    /**
     * 起始加载偏移页
     */
    public RefreshListView setPageStartOffset(int start) {
        this.pageStartOffset = start;
        return this;
    }

    /**
     * 刷新View的类型
     */
    public RefreshListView setViewType(@Type int type) {
        this.mRefreshType = type;
        return this;
    }

    /**
     * 设置回调
     */
    public RefreshListView setListener(IRefreshListener mIRefreshListener) {
        this.mIRefreshListener = mIRefreshListener;
        return this;
    }

    /**
     * 每次加载条目数，与保持接口一致!
     */
    public RefreshListView setPageSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置接口类,让此类负责外部状态
     */
    public RefreshListView setBaseView(BaseView baseView){
        this.mBaseView = baseView;
        return this;
    }

    /**
     * 利用中间类保存数据（此框架的viewModel在横竖屏切换会保持唯一性，可传入viewModel的实现类）
     */
    public RefreshListView setRestoreView(IRestoreView restoreView){
        this.mRestoreView = restoreView;
        return this;
    }

    /**
     * 设置适配器
     */
    public void setAdapter(BaseQuickAdapter mAdapter) {
        this.mAdapter = mAdapter;
        initLogic();
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * 加载总页数，通常由接口获取，若没有可传0（请求发送后）
     */
    @Override
    public void setTotalPage(int totalPage) {
        mOnSaveInstance_totalPage = totalPage;

        this.totalPage = totalPage;
    }

    private void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }

    /**
     * 设置数据源（请求发送后）
     */
    @Override
    public void setData(List beanList, boolean loadMore) {

        mOnSaveInstance_loadMore = loadMore;

        if (beanList == null || beanList.size() == 0) {
            //初次加载为空
            if(mAdapter.getItemCount()==0 && mBaseView!=null) {
                mBaseView.showEmpty();
            }
            return;
        }
        if(mBaseView != null) {
            mBaseView.showContent();
        }
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
                mAdapter.addData(beanList);
                currentPage++;
                setLoadMore(valid);
                if (valid) {
                    mAdapter.loadMoreComplete();
                } else {
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

    @Override
    public void setMessage(Error error, String content){

        mOnSaveInstance_error = error;
        mOnSaveInstance_content = content;

        if(mAdapter.getItemCount() == 0){
            if(mBaseView!=null) mBaseView.showNetError(error, content);
        }
        else{
            if(mBaseView!=null) {
                //错误是由 刷新 还是 加载更多 引发的
                if(mRefreshLayout.getCurrentRefreshStatus()!= RefreshLayout.RefreshStatus.IDLE){
                    mRefreshLayout.endRefreshing();
                    mBaseView.showMessage(content);
                }
                else{
                    mAdapter.loadMoreFail();
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if(mRestoreView!=null){
            SaveInstance saveInstance = mRestoreView.getSaveInstance();
            if(saveInstance.isDataView()){
                setTotalPage(saveInstance.getTotalPage());
                setData(saveInstance.getBeanList(),saveInstance.isLoadMore());
            }
            else{ setMessage(saveInstance.getError(),saveInstance.getContent()); }
            setCurrentPage(saveInstance.getCurrentPageIndex());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if(mRestoreView!=null){
            SaveInstance saveInstance=new SaveInstance();
            saveInstance.setDataView(mAdapter.getItemCount()!=0);
            saveInstance.setTotalPage(mOnSaveInstance_totalPage);
            saveInstance.setData(mAdapter.getData(),mOnSaveInstance_loadMore );
            saveInstance.setMessage(mOnSaveInstance_error, mOnSaveInstance_content);
            saveInstance.setCurrentPage(currentPage);
            mRestoreView.setSaveInstance(saveInstance);
        }
        return super.onSaveInstanceState();
    }

    /**---------------------------------------------getter---------------------------------------------*/

    public int getPageStartOffset() {
        return pageStartOffset;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public RefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }




    /**---------------------------------------------interface---------------------------------------------*/

    public static interface IRestoreView{

        /**
         * 是否已经加载过数据
         */
        boolean isDataView();

        void setSaveInstance(SaveInstance saveInstance);

        SaveInstance getSaveInstance();
    }

    public static interface IRefreshListener {
        void onRefresh(RefreshLayout refreshLayout);

        void onLoadMore(int targetPage);
    }

    @IntDef(value = {Refresh, LoadMore, Refresh_LoadMore})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    public static class SaveInstance<BEAN> extends BaseRefreshViewAdapter<BEAN> {

        private boolean isDataView;

        private int totalPage ;

        private List<BEAN> beanList ;
        private boolean loadMore ;

        private Error error ;
        private String content ;

        private int currentPageIndex;


        public void setCurrentPage(int currentPageIndex){
            this.currentPageIndex =currentPageIndex;
        }

        public void setDataView(boolean isRequestNet){
            this.isDataView = isRequestNet;
        }

        @Override
        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        @Override
        public void setData(List<BEAN> beanList, boolean loadMore) {
            this.beanList = beanList;
            this.loadMore = loadMore;
        }

        @Override
        public void setMessage(Error error, String content) {
            this.error = error;
            this.content = content;
        }

        public boolean isDataView(){
            return isDataView;
        }

        public int getTotalPage(){
            return  totalPage;
        }

        public List<BEAN> getBeanList(){
            return beanList;
        }

        public boolean isLoadMore(){
            return loadMore;
        }

        public Error getError(){
            return error;
        }

        public String getContent(){
            return content;
        }

        //todo
        public int getCurrentPageIndex(){
            return currentPageIndex;
        }

        public void restoreData(BaseRefreshView<BEAN> baseRefreshView){
            if(isDataView()){
                baseRefreshView.setTotalPage(getTotalPage());
                baseRefreshView.setData(getBeanList(), isLoadMore());
            }else{
                baseRefreshView.setMessage(getError(),getContent());
            }
        }
    }
    /**--------------------------------------unUse-----------------------------------------------------*/
    @Override
    public void onRefreshData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNetError(Error error, String content) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showMessage(String content) {

    }

    @Nullable
    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return null;
    }

    @Override
    public void removeViewModel() {

    }

}
