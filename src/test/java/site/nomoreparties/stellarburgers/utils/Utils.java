package site.nomoreparties.stellarburgers.utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static <T extends Serializable> String buildKV(String key, T value) {
        if (value instanceof String) {
            return String.format("\"%s\":\"%s\"", key, value);
        } else {
            return String.format("\"%s\":%s", key, value);
        }
    }

    public static <T> String buildKV(String key, List<T> value) {
        List<String> valueStr;
        if (value.isEmpty()) {
            return String.format("\"%s\":%s", key, Arrays.toString(value.toArray()));
        } else if (value.get(0) instanceof String) {
            valueStr = value.stream().map(s -> String.format("\"%s\"", s)).collect(Collectors.toList());
        } else {
            valueStr = value.stream().map(s -> String.format("%s", s.toString())).collect(Collectors.toList());
        }
        return String.format("\"%s\":%s", key, Arrays.toString(valueStr.toArray()));
    }
}
