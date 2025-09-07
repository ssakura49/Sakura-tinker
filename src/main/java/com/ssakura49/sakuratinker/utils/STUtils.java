package com.ssakura49.sakuratinker.utils;

import com.ssakura49.sakuratinker.utils.java.Exe;
import com.ssakura49.sakuratinker.utils.java.ExeCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class STUtils {
    @SuppressWarnings("StatementWithEmptyBody")
    public static <T> void safelyForEach(final List<T> collection, Exe<T> callable) {
        if (collection.isEmpty()) {
        } else if (collection.size() == 1) callable.run(collection.get(0));
        else if (collection.size() == 2) {
            callable.run(collection.get(0));
            callable.run(collection.get(1));
        } else for (int i = collection.size() - 1; i >= 0; i--)
            callable.run(collection.get(i));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static <T> void safelyForEach(final List<T> collection, ExeCollection<T> callable) {
        if (collection.isEmpty()) {
        } else if (collection.size() == 1) callable.run(collection.get(0), 0);
        else if (collection.size() == 2) {
            callable.run(collection.get(0), 0);
            callable.run(collection.get(1), 1);
        } else for (int i = collection.size() - 1; i >= 0; i--)
            callable.run(collection.get(i), i);
    }

    public static <T, R> List<R> safelyForEach(final List<T> collection, Function<T, R> callable) {
        if (collection.isEmpty()) {
            return List.of();
        } else if (collection.size() == 1) {
            R r = callable.apply(collection.get(0));
            if (r != null)
                return List.of(r);
            else return List.of();
        } else {
            final List<R> rList = new ArrayList<>();
            for (int i = collection.size() - 1; i >= 0; i--) {
                R r = callable.apply(collection.get(i));
                if (r != null)
                    rList.add(r);
            }
            return rList;
        }
    }
}
