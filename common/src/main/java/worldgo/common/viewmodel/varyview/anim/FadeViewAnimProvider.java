package worldgo.common.viewmodel.varyview.anim;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;


public class FadeViewAnimProvider implements ViewAnimProvider {

    @Override
    public Animation showAnimation() {
        Animation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(150);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(false);
        return animation;
    }

    @Override
    public Animation hideAnimation() {
        Animation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(150);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(false);
        return animation;
    }

}
