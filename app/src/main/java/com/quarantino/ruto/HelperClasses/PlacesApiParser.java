package com.quarantino.ruto.HelperClasses;

import java.util.List;

public class PlacesApiParser {

    /**
     * html_attributions : []
     * next_page_token : CrQCJwEAAO8DTNOCWzcAavWknKxIHyrmM5oC80IOtbJZrIctJoKqXE0eUX6nUtaV1afR9BEECqu_YXcNLFpD-zpbe1XW0defUQg3osVEHhe3iH6AJ3Haabla7L0yWQXjeM_T8l4UF3jn-z05RJPvm-pgOFztJ5ED1FeOoQbxLlhAC_vLo065FMKR1RgJ2wI7LP_dO3Ubsu_-Oh5mEZkW1ErQHxUcvCqWX_VgMczJeVuh4PGCNKzJlNQ_rqbXB1uxzhXscL7BwR59a281Z38EhKvB4UvPOgJFzWiwNC-zTVq9ioHTs-BgPmwKwavqiTqL_9cDmV8t_F9BCGhhSdw-nPFHOKmpOTIda4PatiLmR-z1885iDnA02inH2XKShXeytVpfEvAKOF5ohIXD3xlfJLA0Hb-AUJ8SENSNPhM3XoXA9guxpRfxOnoaFPv5fb4tFOu4SSSdDfHjpYp3vbyE
     * results : [{"geometry":{"location":{"lat":28.5631886,"lng":77.33601829999999}},"name":"Botanic Garden of Indian Republic","opening_hours":{"open_now":true},"photos":[{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/103096608337277105428\">Prof Arun C. Mehta<\/a>"],"photo_reference":"CmRaAAAAmBg5b7LjsxoigFDiTP9cgf26uiq2bpQ75OjyTTddorwUQXcCTghwQfC12v9sgAJkJCW4By6HRbjBI5Yihm-iTKY0JuGoQEjyoV_uyDrryBeOPb07tou93H9CWjcGJYWoEhDJQ2Yt0LYySTHoNTwN1OjuGhSN1pRwCUwshS6siRI6SIQ6pV1Syg","width":4032}],"place_id":"ChIJj0a0TMrlDDkRJt769QiSD2c","rating":4.2,"reference":"ChIJj0a0TMrlDDkRJt769QiSD2c","vicinity":"Capt. Viijyant, Captain Vijyant Thapar Marg, Sector 38, Noida"}]
     * status : OK
     */

    private String next_page_token;
    private String status;
    private List<?> html_attributions;
    private List<ResultsBean> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<?> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * geometry : {"location":{"lat":28.5631886,"lng":77.33601829999999}}
         * name : Botanic Garden of Indian Republic
         * opening_hours : {"open_now":true}
         * photos : [{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/103096608337277105428\">Prof Arun C. Mehta<\/a>"],"photo_reference":"CmRaAAAAmBg5b7LjsxoigFDiTP9cgf26uiq2bpQ75OjyTTddorwUQXcCTghwQfC12v9sgAJkJCW4By6HRbjBI5Yihm-iTKY0JuGoQEjyoV_uyDrryBeOPb07tou93H9CWjcGJYWoEhDJQ2Yt0LYySTHoNTwN1OjuGhSN1pRwCUwshS6siRI6SIQ6pV1Syg","width":4032}]
         * place_id : ChIJj0a0TMrlDDkRJt769QiSD2c
         * rating : 4.2
         * reference : ChIJj0a0TMrlDDkRJt769QiSD2c
         * vicinity : Capt. Viijyant, Captain Vijyant Thapar Marg, Sector 38, Noida
         */

        private GeometryBean geometry;
        private String name;
        private OpeningHoursBean opening_hours;
        private String place_id;
        private double rating;
        private String reference;
        private String vicinity;
        private List<PhotosBean> photos;

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public OpeningHoursBean getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(OpeningHoursBean opening_hours) {
            this.opening_hours = opening_hours;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":28.5631886,"lng":77.33601829999999}
             */

            private LocationBean location;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public static class LocationBean {
                /**
                 * lat : 28.5631886
                 * lng : 77.33601829999999
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }

        public static class OpeningHoursBean {
            /**
             * open_now : true
             */

            private boolean open_now;

            public boolean isOpen_now() {
                return open_now;
            }

            public void setOpen_now(boolean open_now) {
                this.open_now = open_now;
            }
        }

        public static class PhotosBean {
            /**
             * height : 3024
             * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/103096608337277105428\">Prof Arun C. Mehta<\/a>"]
             * photo_reference : CmRaAAAAmBg5b7LjsxoigFDiTP9cgf26uiq2bpQ75OjyTTddorwUQXcCTghwQfC12v9sgAJkJCW4By6HRbjBI5Yihm-iTKY0JuGoQEjyoV_uyDrryBeOPb07tou93H9CWjcGJYWoEhDJQ2Yt0LYySTHoNTwN1OjuGhSN1pRwCUwshS6siRI6SIQ6pV1Syg
             * width : 4032
             */

            private int height;
            private String photo_reference;
            private int width;
            private List<String> html_attributions;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getPhoto_reference() {
                return photo_reference;
            }

            public void setPhoto_reference(String photo_reference) {
                this.photo_reference = photo_reference;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<String> getHtml_attributions() {
                return html_attributions;
            }

            public void setHtml_attributions(List<String> html_attributions) {
                this.html_attributions = html_attributions;
            }
        }
    }
}
