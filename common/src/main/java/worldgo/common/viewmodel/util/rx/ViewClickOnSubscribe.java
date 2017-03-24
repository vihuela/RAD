package worldgo.common.viewmodel.util.rx;

import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;


class ViewClickOnSubscribe implements Observable.OnSubscribe<Void> {
    private final View view;

    ViewClickOnSubscribe(View view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super Void> subscriber) {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        view.setOnClickListener(listener);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                view.setOnClickListener(null);
            }
        });
    }
}
