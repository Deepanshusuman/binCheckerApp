package com.assets.binfinder.model.savedlist;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class SavedListConverters {

    @TypeConverter
    public static List<Long> fromString( String value) {
        Type listType = new TypeToken<List<Long>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String fromArrayList( List<Long> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
