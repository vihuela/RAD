package worldgo.rad.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.blankj.utilcode.utils.ScreenUtils;


public class CustomerImageView extends ImageView {
    public CustomerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapperHeightSpec = MeasureSpec.makeMeasureSpec((int) (ScreenUtils.getScreenHeight() * 0.5), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, wrapperHeightSpec);

    }
}
