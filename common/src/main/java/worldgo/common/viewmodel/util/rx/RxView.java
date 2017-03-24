package worldgo.common.viewmodel.util.rx;

import android.support.annotation.NonNull;
import android.view.View;

import rx.Observable;


public class RxView {
    public static Observable<Void> clicks(@NonNull View view) {
        return Observable.create(new ViewClickOnSubscribe(view));
    }
}
