package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public class ErrorResponseList implements Iterable {
    private ArrayList<ErrorResponse> list;

    public ErrorResponseList() {
        list = new ArrayList<>();
    }

    @Override
    public @NotNull Iterator<ErrorResponse> iterator() {
        return list.iterator();
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return list.isEmpty();
    }

    public ErrorResponseList add(@Nullable ErrorResponse err) {
        if (err != null) {
            list.add(err);
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (ErrorResponse err : list) {
            str.append(err.toString());
        }
        return str.toString();
    }
}
