package worldgo.common.viewmodel.refresh.adapters;

import android.databinding.BindingAdapter;

import worldgo.common.viewmodel.refresh.RefreshLayout;


public class RefreshLayoutAdapter {

    @BindingAdapter({"bga_refresh_delegate"})
    public static void setDelegate(RefreshLayout refreshLayout, RefreshLayout.RefreshLayoutDelegate delegate) {
        refreshLayout.setDelegate(delegate);
    }

}