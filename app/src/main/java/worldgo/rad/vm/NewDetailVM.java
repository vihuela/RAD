package worldgo.rad.vm;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.utils.SnackbarUtils;

import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.app.BaseApplication;
import worldgo.common.viewmodel.framework.IView;
import worldgo.common.viewmodel.util.CommonUtils;
import worldgo.rad.request.call.JsonCallback;
import worldgo.rad.request.call.JsonConvert;
import worldgo.rad.request.entity.NewsDetailRequest;
import worldgo.rad.ui.DragPhotoActivity;
import worldgo.rad.ui.NewsDetailActivity;


public class NewDetailVM extends AbsVM<IView> {

    private int id;
    private String url;
    private String body;

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        id = arguments != null ? arguments.getInt("id") : -1;
    }

    public void getNesDetail(INetQueue iNetQueue) {
        mApi.getStoryDetail(id).rx(new JsonConvert<NewsDetailRequest.Res>() {
        }, new JsonCallback<NewsDetailRequest.Res>() {
            @Override
            public void success(NewsDetailRequest.Res res, boolean fromCache) {
                url = res.share_url;
                body = res.body;
                ((NewsDetailActivity) getViewOptional()).setData(res);
            }

            @Override
            public void error(Error error, String message) {
                getViewOptional().showNetError(error, message);
            }
        }, iNetQueue);
    }

    public String convertBody(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }

    public void initWV(final WebView webView) {
        webView.setScrollbarFadingEnabled(true);

        WebSettings webViewSettings = webView.getSettings();
        //能够和js交互
        webViewSettings.setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        webViewSettings.setBuiltInZoomControls(false);
        //缓存
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        webViewSettings.setDomStorageEnabled(true);
        //开启application Cache功能
        webViewSettings.setAppCacheEnabled(false);
//        webViewSettings.setBlockNetworkImage(true);无图模式
        //对图片的点击事件用js调用java方法，详见 https://juejin.im/post/58d9aca80ce463005713dba6
        webView.addJavascriptInterface(new JavascriptInterface(BaseApplication.getAppContext().getCurActivity()), "ImageClickListener");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                openUrl(view, url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //这段js函数的功能就是注册监听，遍历所有的img标签，并添加onClick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                webView.loadUrl("javascript:(function(){"
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++)  " + "{"
                        + "    objs[i].onclick=function()  " + "    {  "
                        + "        window.ImageClickListener.onImageClick(this.src);  "
                        + "    }  " + "}" + "})()");
            }
        });
    }

    public void openUrl(WebView webView, String url) {
        try {
            BaseApplication.getAppContext().getCurActivity().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        } catch (ActivityNotFoundException ex) {
            SnackbarUtils.showShortSnackbar(
                    BaseApplication.getAppContext().getCurActivity().getWindow().getDecorView(),
                    "没有安装浏览器",
                    0xFFFA7C20,
                    0xFF000000);
        }
    }

    public void openBroswer() {
        openUrl(null, url);
    }

    public void copyBodyContent() {
        CommonUtils.copyTextToClipboard(BaseApplication.getAppContext().getCurActivity(), Html.fromHtml(body));
        getViewOptional().showMessage("复制成功");
    }

    class JavascriptInterface {
        private Context context;


        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void onImageClick(String img) {
            DragPhotoActivity.startPhotoActivity(BaseApplication.getAppContext().getCurActivity(), null, img, false);
        }
    }
}
