package com.fab.banggabgo.type;

public enum Seoul {
    GANGNAM("강남구"),
    GANGDONG("강동구"),
    GANGBUK("강북구"),
    GANGSEO("강서구"),
    GWANAK("관악구"),
    GWANGJIN("광진구"),
    GURO("구로구"),
    GEUMCHEON("금천구"),
    NOWON("노원구"),
    DOBONG("도봉구"),
    DONGDAEMUN("동대문구"),
    DONGJAK("동작구"),
    MAPO("마포구"),
    SEODAEMUN("서대문구"),
    SEOCHO("서초구"),
    SEONGDONG("성동구"),
    SEONGBUK("성북구"),
    SONGPA("송파구"),
    YANGCHEON("양천구"),
    YEONGDEUNGPO("영등포구"),
    YONGSAN("용산구"),
    EUNPYEONG("은평구"),
    JONGNO("종로구"),
    JUNGGU("중구"),
    JUNGNANG("중랑구");

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
