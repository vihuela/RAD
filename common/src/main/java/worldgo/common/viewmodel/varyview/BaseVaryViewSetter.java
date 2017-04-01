package worldgo.common.viewmodel.varyview;


import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import worldgo.common.viewmodel.util.ViewUtils;
import worldgo.common.viewmodel.varyview.anim.FadeViewAnimProvider;
import worldgo.common.viewmodel.varyview.anim.ViewAnimProvider;

public abstract class BaseVaryViewSetter {

    private static final int DEFAULT_BG = 0xFFF0F0F0;
    private VaryItem emptyItem;
    private VaryItem errorItem;
    private String loadingContent;
    private int varyViewBackground;
    private ViewAnimProvider viewAnimProvider;

    public BaseVaryViewSetter() {
        emptyItem = setEmpty();
        errorItem = setError();
        loadingContent = setLoadingContent();
        varyViewBackground = (setVaryViewBackground() == 0 || setVaryViewBackground() == -1) ? DEFAULT_BG : setVaryViewBackground();
        viewAnimProvider = setViewAnimProvider();
    }

    static void setText(View root, @IdRes int textView, String content) {
        TextView t = ViewUtils.findById(root, textView);
        t.setText(content);
    }

    static void setImageSrc(View root, @IdRes int imageView, int imageId) {
        ImageView t = ViewUtils.findById(root, imageView);
        t.setImageResource(imageId);
    }

    public abstract String setLoadingContent();

    public abstract VaryItem setEmpty();

    public abstract VaryItem setError();

    public abstract int setVaryViewBackground();

    /**
     * 切换View时的动画
     */
    public abstract ViewAnimProvider setViewAnimProvider();

    /**
     * getter--------------------------------
     */
    VaryItem getEmpty() {
        return emptyItem;
    }

    VaryItem getError() {
        return errorItem;
    }

    String getLoadingContent() {
        return loadingContent;
    }

    int getVaryViewBackground() {
        return varyViewBackground;
    }

    ViewAnimProvider getViewAnimProvider() {
        return viewAnimProvider;
    }

    /**
     * --------------------------------
     */

    protected static class VaryItem {
        int imageId;
        String content;

        public static VaryItem Instance(@DrawableRes int imgId, String content) {
            VaryItem varyItem = new VaryItem();
            varyItem.imageId = imgId;
            varyItem.content = content;
            return varyItem;
        }
    }
}
