package worldgo.common.viewmodel.varyview.anim;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * @author ricky.yao on 2017/4/5.
 */

public class FadeVaryViewAnim implements VaryViewAnimProvider {
    @Override
    public ValueAnimator showAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(200);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        return valueAnimator;
    }

    @Override
    public ValueAnimator hideAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0).setDuration(200);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        return valueAnimator;
    }
}
