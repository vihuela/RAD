package worldgo.rad.request.util;

import ricky.oknet.utils.ApiHelper;
import worldgo.rad.request.Api;

public class ApiUtil {

    public static Api getApi() {
        return ApiHelper.get(Api.class);
    }

}
