package worldgo.rad.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;

import worldgo.common.viewmodel.aop.anno.Trace;
import worldgo.common.viewmodel.framework.binding.ViewModelBindingConfig;
import worldgo.rad.R;
import worldgo.rad.base.BaseBindingActivity;
import worldgo.rad.databinding.ActivityMainBinding;
import worldgo.rad.vm.MainActivityVM;

public class MainActivity extends BaseBindingActivity<MainActivity, MainActivityVM, ActivityMainBinding> {


    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, null);
    }


    @Trace
    @Override
    public ViewModelBindingConfig getViewModelBindingConfig() {
        return new ViewModelBindingConfig(R.layout.activity_main, this);
    }

}
