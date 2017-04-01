package worldgo.rad.vm;

import android.support.annotation.NonNull;

import com.blankj.utilcode.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ricky.oknet.lifecycle.INetQueue;
import ricky.oknet.utils.Error;
import worldgo.common.viewmodel.refresh.RefreshListView;
import worldgo.common.viewmodel.refresh.interfaces.IRefreshView;
import worldgo.rad.request.call.JsonCallback;
import worldgo.rad.request.entity.NewsRequest;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class NewsFragmentVM extends AbsVM<IRefreshView> implements RefreshListView.IRestoreInstance {

    private RefreshListView.SaveInstance mOnSaveInstance;
    private int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


    public void getNewForLast(INetQueue mNetQueue) {
        mApi.getNewForLast().execute(new JsonCallback<NewsRequest.Res>() {
            @Override
            public void success(NewsRequest.Res res, boolean fromCache) {
                getViewOptional().getRefreshView().setData(res.stories, false);
            }

            @Override
            public void error(Error error, String message) {
                super.error(error, message);
                getViewOptional().getRefreshView().setMessage(error, message);
            }
        }, mNetQueue);
    }

    public void getNewForDate(int index, INetQueue mNetQueue) {
        String dateString = getNextDay(-1 * index);
        mApi.getNewForDate(dateString).execute(new JsonCallback<NewsRequest.Res>() {
            @Override
            public void success(NewsRequest.Res res, boolean fromCache) {
                getViewOptional().getRefreshView().setData(res.stories, true);
            }

            @Override
            public void error(Error error, String message) {
                super.error(error, message);
                getViewOptional().getRefreshView().setMessage(error, message);
            }
        }, mNetQueue);
    }

    @Override
    public boolean isLoaded() {
        return mOnSaveInstance != null && mOnSaveInstance.isLoaded();
    }

    @Override
    public RefreshListView.SaveInstance getSaveInstance() {
        return mOnSaveInstance;
    }

    @Override
    public void setSaveInstance(@NonNull RefreshListView.SaveInstance saveInstance) {
        mOnSaveInstance = saveInstance;
    }

    private String getNextDay(int delay) {
        try {
            String formatter = "yyyyMMdd";
            SimpleDateFormat format = new SimpleDateFormat(formatter);
            String mdate = "";
            Date d = TimeUtils.string2Date(TimeUtils.date2String(TimeUtils.getNowTimeDate(), formatter), formatter);
            long myTime = (d.getTime() / 1000) + delay * 24
                    * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }


}
