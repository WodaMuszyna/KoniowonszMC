package xyz.wodamuszyna.koniowonsz.database;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.utils.Logger;
import xyz.wodamuszyna.koniowonsz.utils.TimeUtil;
import xyz.wodamuszyna.koniowonsz.utils.Timing;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLConnection implements IConnection{
    private final String host;
    private final String user;
    private final String pass;
    private final String name;
    private final int port;
    private Connection conn;
    private long time;
    private Executor executor;
    private AtomicInteger ai;

    public MySQLConnection(String host, int port, String user, String pass, String name) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.executor = Executors.newSingleThreadExecutor();
        this.time = System.currentTimeMillis();
        this.ai = new AtomicInteger();
        (new BukkitRunnable() {
            public void run() {
                if (System.currentTimeMillis() - MySQLConnection.this.time > TimeUtil.SECOND
                        .getTime(30)) {
                    MySQLConnection.this.update(false, "DO 1");
                }
            }
        }).runTaskTimer(Main.getInstance(), 600L, 600L);
    }

    public boolean connect() {
        Timing t = (new Timing("MySQL ping")).start();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host +
                    ":" + this.port + "/" + this.name, this.user, this.pass);
            Logger.info("Polaczono z baza danych. Ping: " + t.stop().getExecutingTime() + "ms!");
            return true;
        } catch (ClassNotFoundException e) {
            Logger.warning("JDBC driver not found!",
                    "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e2) {
            Logger.warning("Can not connect to a MySQL server!",
                    "Error: " + e2.getMessage());
            e2.printStackTrace();
        }
        return false;
    }

    public void update(boolean now, final String update) {
        this.time = System.currentTimeMillis();
        Runnable r = () -> {
            try {
                if (MySQLConnection.this.conn == null) {
                    MySQLConnection.this.connect();
                }
                MySQLConnection.this.conn.createStatement().executeUpdate(
                        update);
            } catch (SQLException e) {
                Logger.warning("An error occurred with given query '" +
                                update + "'!",
                        "Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
        if (now) {
            r.run();
        } else {
            this.executor.execute(r);
        }
    }

    public ResultSet update(String update) {
        try {
            Statement statement = this.conn.createStatement();
            statement.executeUpdate(update, 1);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            Logger.warning("An error occurred with given query ''!",
                    "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                Logger.warning("Can not close the connection to the MySQL server!",
                        "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void reconnect() { connect(); }


    public boolean isConnected() {
        try {
            return !(this.conn.isClosed() && this.conn != null);
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
    public ResultSet query(String query) {
        try {
            return this.conn.createStatement().executeQuery(
                    query);
        } catch (SQLException e) {
            Logger.warning("An error occurred with given query '" +
                            query + "'!",
                    "Error: " + e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    public void query(String query, Callback<ResultSet> cb) {
        (new Thread(() -> {
            try {
                ResultSet rs = MySQLConnection.this.conn
                        .createStatement()
                        .executeQuery(
                                query);
                cb.done(rs);
            } catch (SQLException e) {
                Logger.warning("An error occurred with given query '" +
                                query + "'!",
                        "Error: " + e.getMessage());
                cb.error(e);
                e.printStackTrace();
            }
        }, "MySQL Thread #" + this.ai.getAndIncrement())).start(); }



    public Connection getConnection() { return this.conn; }
}
