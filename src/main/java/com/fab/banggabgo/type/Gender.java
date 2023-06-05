package com.fab.banggabgo.type;

public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }
}
