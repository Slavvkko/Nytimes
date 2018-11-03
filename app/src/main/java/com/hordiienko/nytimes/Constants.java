package com.hordiienko.nytimes;

public class Constants {
    public static final String BASE_URL = "http://api.nytimes.com/svc/mostpopular/v2/";
    public static final String SECTION = "all-sections";
    public static final int PERIOD = 30;

    public enum ApiType {
        MOSTEMAILED("mostemailed", R.string.mostemailed),
        MOSTSHARED("mostshared", R.string.mostshared),
        MOSTVIEWED("mostviewed", R.string.mostviewed);

        private String name;
        private int title;

        ApiType(String name, int title) {
            this.name = name;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public int getTitle() {
            return title;
        }
    }
}
