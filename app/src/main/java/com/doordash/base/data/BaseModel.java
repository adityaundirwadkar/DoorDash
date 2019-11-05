package com.doordash.base.data;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.zip.CRC32;

public class BaseModel implements Cloneable {

    public String toString() {
        return new Gson().toJson(this);
    }

    public String quickHash(String... data) {
        String s = TextUtils.join("-", data);
        CRC32 c = new CRC32();
        c.update(s.getBytes());
        return Long.toHexString(c.getValue());
    }

    @Override
    public BaseModel clone() {
        try {
            return (BaseModel) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }
}
