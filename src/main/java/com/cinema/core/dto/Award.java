package com.cinema.core.dto;

import java.util.Arrays;

public class Award {
    private short count;
    private AwardType type;
    private boolean achieve;

    public short getCount() {
        return count;
    }

    public void setCount(short count) {
        this.count = count;
    }

    public AwardType getType() {
        return type;
    }

    public void setType(AwardType type) {
        this.type = type;
    }

    public boolean isAchieve() {
        return achieve;
    }

    public void setAchieve(boolean achieve) {
        this.achieve = achieve;
    }

    @Override
    public String toString() {
        return "Award{" +
                "count=" + count +
                ", type=" + type +
                ", achieve=" + achieve +
                '}';
    }

    public static Award buildAward(AwardType type, Short count, Boolean isAchieved) {
        Award award = new Award();
        award.setType(type);
        award.setCount(count);
        award.setAchieve(isAchieved);
        return award;
    }

    public enum AwardType {
        GoldenGlobe("GoldenGlobe"),
        Oscar("Oscar");
        private String value;

        AwardType(String value) {
            this.value = value;
        }

        public static AwardType awardTypeOf(String value) {
            return Arrays.stream(AwardType.values()).filter(awardType -> value.contains(awardType.value)).findFirst().orElse(null);
        }
    }
}
