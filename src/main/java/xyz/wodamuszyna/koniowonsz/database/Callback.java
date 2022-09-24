package xyz.wodamuszyna.koniowonsz.database;

import java.sql.ResultSet;

public interface Callback<T> {
    ResultSet done(ResultSet param);

    void error(Throwable cause);
}
