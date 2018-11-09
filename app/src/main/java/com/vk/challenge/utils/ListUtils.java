package com.vk.challenge.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class ListUtils {


    public interface Filter<T> {
        boolean accept(T t);
    }

    public interface Map<T, P> {
        P map(T object);
    }

    public static <T, P> List<P> map(List<T> list, Map<T, P> map) {
        if (list == null) {
            return null;
        }
        List<P> result = new ArrayList<>(list.size());
        for (T t : list) {
            result.add(map.map(t));
        }
        return result;
    }

    @Nullable
    public static <T> List<T> filter(@Nullable List<T> list, Filter<T> filter) {
        if (list == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (T object : list) {
            if (filter.accept(object)) {
                result.add(object);
            }
        }
        return result;
    }

    @Nullable
    public static <T> T find(List<T> list, Filter<T> criteria) {
        if (list == null) {
            return null;
        }
        for (T object : list) {
            if (criteria.accept(object)) {
                return object;
            }
        }
        return null;
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = tokens.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    public static <T> List<T> distinct(List<T> list) {
        return new ArrayList<>(new LinkedHashSet<>(list));
    }


}
