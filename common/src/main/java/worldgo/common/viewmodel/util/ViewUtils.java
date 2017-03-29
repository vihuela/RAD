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
}
