package worldgo.common.viewmodel.framework.base.view;

import java.util.List;

import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.IView;


public interface BaseRefreshView<BEAN> extends IView {


    void setTotalPage(int totalPage);

    void setData(List<BEAN> beanList, boolean loadMore);

    void setMessage(Error error, String content);

    void onRefreshData();

}
