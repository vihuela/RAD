package worldgo.rad.request.entity;

import java.util.List;

import ricky.oknet.utils.INoProguard;

/**
 * @author ricky.yao on 2017/3/27.
 */

public class ImageListRequest {


    public static class Res implements INoProguard {


        public boolean error;
        public List<Item> results;

        public static class Item implements INoProguard {
            public String _id;
            public String createdAt;
            public String desc;
            public String publishedAt;
            public String source;
            public String type;
            public String url;
            public boolean used;
            public String who;
        }


    }
}
