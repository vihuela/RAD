package worldgo.rad.vm;

import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.common.viewmodel.framework.IView;
import worldgo.rad.request.Api;
import worldgo.rad.request.util.ApiUtil;


public abstract class AbsVM<T extends IView> extends AbstractViewModel<T> {
    protected Api mApi = ApiUtil.getApi();
}
