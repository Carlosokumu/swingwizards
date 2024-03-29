package com.example.smarttrader.data.local.convertors

import androidx.room.TypeConverter
import com.example.smarttrader.data.local.entity.ProtoOALightSymbolEntity
import com.example.smarttrader.models.SymbolModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ProtoOALightSymbolListConvertor {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<ProtoOALightSymbolEntity> {
        val listType = object : TypeToken<List<ProtoOALightSymbolEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<ProtoOALightSymbolEntity>): String {
        return gson.toJson(list)
    }
}