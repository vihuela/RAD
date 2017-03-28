package worldgo.common.viewmodel.framework.base.view;

import java.util.List;

import worldgo.common.viewmodel.framework.IView;


public interface BaseRefreshView<BEAN> extends IView {


    void setTotalPage(int totalPage);

    void setData(List<BEAN> beanList, boolean loadMore);

}
