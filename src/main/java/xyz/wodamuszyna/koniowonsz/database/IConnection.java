package xyz.wodamuszyna.koniowonsz.database;

import java.sql.Connection;
import java.sql.ResultSet;

public interface IConnection {
    Connection getConnection();

    boolean connect();

    void disconnect();

    void reconnect();

    boolean isConnected();

    ResultSet query(String query);

    void query(String query, Callback<ResultSet> callback);

    void update(boolean b, String query);

    ResultSet update(String query);

}
