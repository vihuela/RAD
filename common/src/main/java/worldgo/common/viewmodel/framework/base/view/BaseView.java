package worldgo.common.viewmodel.framework.base.view;


import ricky.oknet.utils.Error;

/**
 * show message
 */
public interface BaseView {
    void showLoading();

    void showNetError(Error error, String content);//combine with oknet

    void showEmpty();

    void showContent();

    void showMessage(String content);
}
