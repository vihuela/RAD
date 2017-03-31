package worldgo.rad.request;

import ricky.oknet.retrofit.Net;
import ricky.oknet.retrofit.anno.GET;
import ricky.oknet.retrofit.anno.Path;
import worldgo.rad.request.entity.ImageListRequest;

/**
 * @author ricky.yao on 2017/3/27.
 */

public interface Api {

    int LIST_SIZE = 50;

    @GET("福利/{size}/{page}")
    Net<ImageListRequest.Res> getImageList(@Path("size") int size, @Path("page") int page);
}
