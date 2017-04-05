package worldgo.rad.request;

import ricky.oknet.cache.CacheMode;
import ricky.oknet.retrofit.Net;
import ricky.oknet.retrofit.anno.CACHE;
import ricky.oknet.retrofit.anno.GET;
import ricky.oknet.retrofit.anno.Path;
import worldgo.rad.request.entity.ImageListRequest;
import worldgo.rad.request.entity.NewsRequest;

/**
 * @author ricky.yao on 2017/3/27.
 */

public interface Api {

    int LIST_SIZE = 50;

    @GET("福利/{size}/{page}")
    Net<ImageListRequest.Res> getImageList(@Path("size") int size, @Path("page") int page);

    @CACHE(CacheMode.REQUEST_FAILED_READ_CACHE)
    @GET("http://news-at.zhihu.com/api/4/news/latest")
    Net<NewsRequest.Res> getNewForLast();

    @GET("http://news-at.zhihu.com/api/4/news/before/{date}")
    Net<NewsRequest.Res> getNewForDate(@Path("date") String date);

}
