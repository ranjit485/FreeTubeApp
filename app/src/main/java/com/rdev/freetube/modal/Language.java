package com.rdev.freetube.modal;

import com.squareup.moshi.Json;

public class Language {
    @Json(name = "name")
    private String name;

    @Json(name = "code")
    private String code;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
