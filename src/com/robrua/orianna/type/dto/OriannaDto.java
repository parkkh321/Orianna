package com.robrua.orianna.type.dto;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class OriannaDto implements Serializable {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final long serialVersionUID = 8004053822022503790L;

    /**
     * Gets a JSON representation of the Dto
     *
     * @return a JSON representation of the Dto
     */
    public String toJSON() {
        return GSON.toJson(this);
    }
}
