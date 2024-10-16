package cn.travellerr.BlueArchive;

import cn.travellerr.tools.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class GetSentenceApi {
    static String fortuneSummary;
    static String signText;
    static String unSignText;
    static String luckyStar;

    static int AllFortuneID = 402;

    public static void generateFortuneID(long qqNumber, long botId, boolean isForJrys) {
        String directory = "./data/cn.travellerr.AronaBot/";
        String dbName = "Jrys.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }
        createDirectory(directory);

        // 创建目录，如果已存在则不会创建

        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            if (isQQNumberNew(conn, qqNumber)) {
                int fortuneID = generateFortuneID(qqNumber, botId);
                insertData(conn, qqNumber, fortuneID);
            }
            if (isDateDifferent(conn, qqNumber)) {
                int fortuneID = generateFortuneID(qqNumber, botId);
                updateData(conn, qqNumber, fortuneID);
            }
            if (isForJrys) getData(conn, qqNumber);
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }

    public static boolean isDateDifferent(long qqNumber) {
        String directory = "./data/cn.travellerr.AronaBot/";
        String dbName = "Jrys.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }


        createDirectory(directory);
        // 创建目录，如果已存在则不会创建

        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            String selectSql = "SELECT MAX(Date) FROM fortune WHERE QQ = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setLong(1, qqNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        Timestamp latestDate = rs.getTimestamp(1);
                        if (latestDate != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String latestDateString = dateFormat.format(latestDate);
                            String currentDateString = dateFormat.format(new Date());
                            return !latestDateString.equals(currentDateString);
                        }
                    }
                }
            }
            return true; // 默认返回true，表示日期不相等
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }

    public static boolean isQQNumberNew(long qqNumber) {
        String directory = "./data/cn.travellerr.AronaBot/";
        String dbName = "Jrys.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }

        createDirectory(directory);
        // 创建目录，如果已存在则不会创建

        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            String selectSql = "SELECT COUNT(*) FROM fortune WHERE QQ = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setLong(1, qqNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }


    private static void updateData(Connection conn, long qqNumber, int fortuneID) throws SQLException {
        String updateSql = "UPDATE fortune SET fortuneID = ?, Date = ? WHERE QQ = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, fortuneID);
            updateStmt.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            updateStmt.setLong(3, qqNumber);
            updateStmt.executeUpdate();
        }
    }


    private static void createDirectory(String directory) {
        Path path = Paths.get(directory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            Log.error("出错了~", e);
        }
    }

    private static boolean isQQNumberNew(Connection conn, long qqNumber) throws SQLException {
        String selectSql = "SELECT COUNT(*) FROM fortune WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    private static int generateFortuneID(long userId, long botId) {
        Random random = new Random(System.currentTimeMillis() + userId + botId);
        return random.nextInt(AllFortuneID) + 1;
    }

    private static boolean isDateDifferent(Connection conn, long qqNumber) throws SQLException {
        String selectSql = "SELECT MAX(Date) FROM fortune WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp latestDate = rs.getTimestamp(1);
                    if (latestDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String latestDateString = dateFormat.format(latestDate);
                        String currentDateString = dateFormat.format(new Date());
                        return !latestDateString.equals(currentDateString);
                    }
                }
            }
        }
        return true; // 默认返回true，表示日期不相等
    }


    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS fortune (" +
                "ID INTEGER PRIMARY KEY, " +
                "QQ INTEGER, " +
                "fortuneID INTEGER, " + // 将Id字段重命名为fortuneID
                "Date TEXT DEFAULT (strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')))";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    private static void insertData(Connection conn, long qqNumber, int ID) {
        String sql = "INSERT INTO fortune (QQ, fortuneID) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
            // 设置参数值
            insertStmt.setLong(1, qqNumber);
            insertStmt.setInt(2, ID);

            // 执行插入操作
            insertStmt.executeUpdate();
            /*int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
            }*/
        } catch (SQLException e) {
            // 插入失败时的异常处理
            throw new RuntimeException("出错了~", e);
        }
    }

    private static void getData(Connection conn, long qqNumber) {
        String selectSql = "SELECT fortuneID FROM fortune WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int fortuneID = rs.getInt("fortuneID");
                    // 从resources目录下获取JSON文件的输入流
                    try (InputStream inputStream = GetSentenceApi.class.getClassLoader().getResourceAsStream("jrys/jrys.json");
                         Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                        if (scanner.hasNext()) {
                            String json = scanner.useDelimiter("\\A").next();
                            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                            if (jsonObject.has(String.valueOf(fortuneID))) {
                                JsonArray fortuneArray = jsonObject.getAsJsonArray(String.valueOf(fortuneID));
                                if (!fortuneArray.isJsonNull() && !fortuneArray.isEmpty()) {
                                    JsonObject fortuneObject = fortuneArray.get(0).getAsJsonObject();
                                    fortuneSummary = getStringFromJson(fortuneObject, "fortuneSummary");
                                    luckyStar = getStringFromJson(fortuneObject, "luckyStar");
                                    signText = getStringFromJson(fortuneObject, "signText");
                                    unSignText = getStringFromJson(fortuneObject, "unSignText");
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("无法找到或读取jrys.json文件", e);
                    }
                }
            }
        } catch (SQLException e) {
            // 处理SQL异常
            throw new RuntimeException("出错了~", e);
        }
    }

    public static int getFortuneID(long qqNumber) {
        String directory = "./data/cn.travellerr.AronaBot/";
        String dbName = "Jrys.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }

        createDirectory(directory);
        // 创建目录，如果已存在则不会创建

        try (Connection conn = DriverManager.getConnection(url)) {

            createTable(conn);

            String selectSql = "SELECT fortuneID FROM fortune WHERE QQ = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setLong(1, qqNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("fortuneID");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }

        return -1;
    }

    private static String getStringFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }


}
