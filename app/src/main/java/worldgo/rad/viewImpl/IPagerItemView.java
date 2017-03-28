package worldgo.rad.viewImpl;

import worldgo.common.viewmodel.framework.IView;
import worldgo.rad.request.entity.ImageListRequest;

/**
 * @author ricky.yao on 2017/3/23.
 */

public interface IPagerItemView extends IView {
    void setData(ImageListRequest.Res data);
}
