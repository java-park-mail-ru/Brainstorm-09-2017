package application.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


// Обертка над ArrayList<ErrorResponse> реализующая фильтр null при добавлении
public class ErrorResponseList {
    private ArrayList<ErrorResponse> list;


    public ErrorResponseList() {
        list = new ArrayList<>();
    }

    @JsonCreator
    public ErrorResponseList(ArrayList<ErrorResponse> errors) {
        list = errors;
    }


    public ErrorResponseList add(@Nullable ErrorResponse err) {
        if (err != null) {
            list.add(err);
        }
        return this;
    }


    public List<ErrorResponse> getList() { return list; }
}
