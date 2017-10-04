package application.views;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ErrorResponseList extends ArrayList<ErrorResponse> {
    @Override
    public boolean add(@Nullable ErrorResponse err) {
        return err != null && super.add(err);
    }
}

