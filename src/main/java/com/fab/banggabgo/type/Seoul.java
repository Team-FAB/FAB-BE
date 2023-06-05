package com.fab.banggabgo.type;

public enum Seoul {
    GANGNAM("강남"),
    GANGDONG("강동"),
    GANGBUK("강북"),
    GANGSEO("강서"),
    GWANAK("관악"),
    GWANGJIN("광진"),
    GURO("구로"),
    GEUMCHEON("금천"),
    NOWON("노원"),
    DOBONG("도봉"),
    DONGDAEMUN("동대문"),
    DONGJAK("동작"),
    MAPO("마포"),
    SEODAEMUN("서대문"),
    SEOCHO("서초"),
    SEONGDONG("성동"),
    SEONGBUK("성북"),
    SONGPA("송파"),
    YANGCHEON("양천"),
    YEONGDEUNGPO("영등포"),
    YONGSAN("용산"),
    EUNPYEONG("은평"),
    JONGNO("종로"),
    JUNGGU("중구"),
    JUNGNANG("중랑");

    private final String value;

    Seoul(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Seoul fromValue(String value) {
        for (Seoul seoul : Seoul.values()) {
            if (seoul.getValue().equals(value)) {
                return seoul;
            }
        }
        throw new IllegalArgumentException("Invalid Seoul value: " + value);
    }
}
