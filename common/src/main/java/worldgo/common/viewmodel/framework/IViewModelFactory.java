package worldgo.common.viewmodel.framework;

import android.support.annotation.Nullable;

public interface IViewModelFactory <T extends IView, R extends AbstractViewModel<T>> {

    @Nullable
    R createViewModel();
}