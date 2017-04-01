package worldgo.common.viewmodel.refresh.base;

import java.util.List;

import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.refresh.interfaces.IRefreshView;


public interface BaseRefreshView  {

    void setTotalPage(int totalPage);

    void setData(List beanList, boolean loadMore);

    void setMessage(Error error, String content);
}
