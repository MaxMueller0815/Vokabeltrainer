package com.example.puppetmaster.vokabeltrainer.models.way;


public class WayByTime implements Way {

    private int wordsPerHour;
    private int getUpHour;
    private int getUpMinute;
    private int goBedHour;
    private int goBedMinute;

    private WayByTime() {
    }

    @Override
    public int getWayCode() {
        return 0;
    }

    public int getWordsPerHour() {
        return wordsPerHour;
    }

    public int getGetUpHour() {
        return getUpHour;
    }

    public int getGetUpMinute() {
        return getUpMinute;
    }

    public int getGoBedHour() {
        return goBedHour;
    }

    public int getGoBedMinute() {
        return goBedMinute;
    }

    public static Builder newBuilder() {
        return new WayByTime().new Builder();
    }

    public class Builder {

        public Builder setWordsPerHour(int wordsPerHour) {
            WayByTime.this.wordsPerHour = wordsPerHour;
            return this;
        }

        public Builder setGetUpHour(int getUpHour) {
            WayByTime.this.getUpHour = getUpHour;
            return this;
        }

        public Builder setGetUpMinute(int getUpMinute) {
            WayByTime.this.getUpMinute = getUpMinute;
            return this;
        }

        public Builder setGoBedHour(int goBedHour) {
            WayByTime.this.goBedHour = goBedHour;
            return this;
        }

        public Builder setGoBedMinute(int goBedMinute) {
            WayByTime.this.goBedMinute = goBedMinute;
            return this;
        }

        public WayByTime build() {
            return WayByTime.this;
        }
    }

    @Override
    public String toString() {
        return "WayByTime{" +
                "wordsPerHour=" + wordsPerHour +
                ", getUpHour=" + getUpHour +
                ", getUpMinute=" + getUpMinute +
                ", goBedHour=" + goBedHour +
                ", goBedMinute=" + goBedMinute +
                '}';
    }
}
