package cn.jdywl.driver.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wuwantao on 16/4/3.
 */
public class CarPhotosEntity {
    /**
     * vin : LHGRU1843G2007778
     * id : 143
     * images : [{"url":"http://192.168.1.102/laravel/public/images/9/yihu.jpg","thumbnail":"http://192.168.1.102/laravel/public/images/9/thumbnail_yihu.jpg"},{"url":"http://192.168.1.102/laravel/public/images/9/yihu2.jpg","thumbnail":"http://192.168.1.102/laravel/public/images/9/thumbnail_yihu2.jpg"}]
     */

    @SerializedName("vins")
    private List<VinsEntity> vins;

    public List<VinsEntity> getVins() {
        return vins;
    }

    public void setVins(List<VinsEntity> vins) {
        this.vins = vins;
    }

    public static class VinsEntity {
        @SerializedName("vin")
        private String vin;
        @SerializedName("id")
        private int id;
        /**
         * url : http://192.168.1.102/laravel/public/images/9/yihu.jpg
         * thumbnail : http://192.168.1.102/laravel/public/images/9/thumbnail_yihu.jpg
         */

        @SerializedName("images")
        private List<ImagesEntity> images;

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ImagesEntity> getImages() {
            return images;
        }

        public void setImages(List<ImagesEntity> images) {
            this.images = images;
        }

        public static class ImagesEntity {
            @SerializedName("url")
            private String url;
            @SerializedName("thumbnail")
            private String thumbnail;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }
    }
}
