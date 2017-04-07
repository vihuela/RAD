/*
 * Copyright (c) 2016. WorldGo Technology Co., Ltd
 * DO NOT DIVULGE
 */

package worldgo.common.viewmodel.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.CheckResult;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricky.yao on 2016/5/31.
 */
public class ViewUtils {
    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"}) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull Dialog dialog, @IdRes int id) {
        return (T) dialog.findViewById(id);
    }

    public static <T extends View> void apply(@NonNull List<T> list,
                                              @NonNull Action<? super T> action) {
        for (int i = 0, count = list.size(); i < count; i++) {
            action.apply(list.get(i), i);
        }
    }

    /**
     * Apply the specified {@code actions} across the {@code list} of views.
     */
    @SafeVarargs
    public static <T extends View> void apply(@NonNull List<T> list,
                                              @NonNull Action<? super T>... actions) {
        for (int i = 0, count = list.size(); i < count; i++) {
            for (Action<? super T> action : actions) {
                action.apply(list.get(i), i);
            }
        }
    }

    public static List<View> generateViews(View root, @IdRes int... ids) {
        List<View> views = new ArrayList<>();
        for (int id : ids) {
            views.add(findById(root, id));
        }
        return views;
    }

    public interface Action<T extends View> {
        /**
         * Apply the action on the {@code view} which is at {@code index} in the list.
         */
        void apply(@NonNull T view, int index);
    }
}
