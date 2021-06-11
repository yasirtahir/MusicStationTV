package com.yasir.musicstation.tv.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yasir.musicstation.tv.models.base.ServiceDate;
import com.yasir.musicstation.tv.models.base.ServiceDateTime;
import com.yasir.musicstation.tv.models.base.ServiceTime;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GSonFactory {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Gson configuredGSon;

    public static Gson getConfiguredGSon() {
        if (configuredGSon == null) {
            GsonBuilder builder = new GsonBuilder();
            // Date
            builder.registerTypeAdapter(ServiceDate.class, new ServiceDateSerializer(DATE_FORMAT));
            // Time
            builder.registerTypeAdapter(ServiceTime.class, new ServiceTimeSerializer(TIME_FORMAT));
            // Date Time
            builder.registerTypeAdapter(ServiceDateTime.class, new ServiceDateTimeSerializer(DATE_TIME_FORMAT));
            // java.util.Date
            builder.registerTypeAdapter(Date.class, new DateSerializer(DATE_TIME_FORMAT));
            configuredGSon = builder.serializeNulls().create();
        }
        return configuredGSon;
    }

    private static class ServiceTimeSerializer implements JsonSerializer<ServiceTime>, JsonDeserializer<ServiceTime> {

        private SimpleDateFormat sf;

        ServiceTimeSerializer(String format) {
            sf = new SimpleDateFormat(format);
        }

        @Override
        public JsonElement serialize(ServiceTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(sf.format(src.getDate()));
        }

        @Override
        public ServiceTime deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            ServiceTime time = new ServiceTime();
            try {
                time.setDate(sf.parse(json.getAsJsonPrimitive().getAsString()));
            } catch (ParseException e) {
                return null;
            }
            return time;
        }
    }

    private static class ServiceDateTimeSerializer implements JsonSerializer<ServiceDateTime>, JsonDeserializer<ServiceDateTime> {

        private SimpleDateFormat sf;

        ServiceDateTimeSerializer(String format) {
            sf = new SimpleDateFormat(format);
        }

        @Override
        public ServiceDateTime deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            ServiceDateTime time = new ServiceDateTime();
            try {
                time.setDate(sf.parse(json.getAsJsonPrimitive().getAsString()));
            } catch (ParseException e) {
                return null;
            }
            return time;
        }

        @Override
        public JsonElement serialize(ServiceDateTime src, Type arg1, JsonSerializationContext arg2) {
            return new JsonPrimitive(sf.format(src.getDate()));
        }
    }

    private static class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private SimpleDateFormat sf;

        DateSerializer(String format) {
            sf = new SimpleDateFormat(format);
        }

        @Override
        public Date deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            Date time;
            try {
                time = sf.parse(json.getAsJsonPrimitive().getAsString());
            } catch (ParseException e) {
                return null;
            }
            return time;
        }

        @Override
        public JsonElement serialize(Date src, Type arg1, JsonSerializationContext arg2) {
            return new JsonPrimitive(sf.format(src));
        }
    }

    private static class ServiceDateSerializer implements JsonSerializer<ServiceDate>, JsonDeserializer<ServiceDate> {
        private SimpleDateFormat sf;

        ServiceDateSerializer(String format) {
            sf = new SimpleDateFormat(format);
        }

        @Override
        public JsonElement serialize(ServiceDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(sf.format(src.getDate()));
        }

        @Override
        public ServiceDate deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            ServiceDate date = new ServiceDate();
            try {
                date.setDate(sf.parse(json.getAsJsonPrimitive().getAsString()));
            } catch (ParseException e) {
                return null;
            }
            return date;
        }
    }
}