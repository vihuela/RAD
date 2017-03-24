package worldgo.rad.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import worldgo.common.viewmodel.aop.anno.SingleClick;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingFragment;
import worldgo.rad.databinding.FragmentPagerItemBinding;
import worldgo.rad.viewImpl.IPagerItemView;
import worldgo.rad.vm.PagerItemVM;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class PagerItemFragment extends BaseBindingFragment<IPagerItemView, PagerItemVM, FragmentPagerItemBinding> implements IPagerItemView{
    public String mTitle;

    public static PagerItemFragment getInstance(String title) {
        PagerItemFragment sf = new PagerItemFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        sf.setArguments(args);
        return sf;
    }

    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return new ViewModelBindingConfig(R.layout.fragment_pager_item, mContext);
    }

    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString("title");
    }

    @Override
    protected void onFirstUserVisible() {

    }


}
