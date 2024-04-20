package cn.travellerr.websocket;

import cn.travellerr.tools.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VoiceWebSocketClient extends WebSocketClient {
    //static String msg;
    public static String errorMsg = null;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public VoiceWebSocketClient(URI uri) {
        super(uri);
    }

    public static void webSocket(String character, String msg, String lang, String url) throws InterruptedException, URISyntaxException {
        String serverURI = "wss://" + url + "/queue/join"; // 连接的服务器 URI
        VoiceWebSocketClient client = new VoiceWebSocketClient(new URI(serverURI));
        client.connect(); // 连接 WebSocket 服务器
        //System.out.println("Connecting to server...");
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            Thread.sleep(1000); // 等待连接成功
            //System.out.println("Connecting...");
        }
        //System.out.println("Connected.");
        // 发送消息
        String message = "{\"fn_index\":2,\"session_hash\":\"yp6mqefrdna\"}";
        client.send(message);
        //System.out.println("Sent message to server: " + message);
        String voiceUrl;
        try {
            String receivedMessage;
            do {
                receivedMessage = client.getNextMessage();
                if (receivedMessage.contains("send_data")) {
                    if (lang == null) {
                        message = "{\"data\":[\"" + msg + "\",\"" + character + "\",\"简体中文\",0.6,0.668,1,false],\"event_data\":null,\"fn_index\":2,\"session_hash\":\"yp6mqefrdna\"}";
                    } else {
                        message = "{\"data\":[\"" + msg + "\",\"" + character + "\",\"" + lang + "\",0.6,0.668,1,false],\"event_data\":null,\"fn_index\":2,\"session_hash\":\"yp6mqefrdna\"}";
                    }

                    client.send(message);
                    //System.out.println("Sent message to server: " + message);
                }
            } while (!receivedMessage.contains("process_completed"));
            voiceUrl = receivedMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.close(); // 关闭 WebSocket 连接
        //System.out.println("Connection closed.");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(voiceUrl, JsonObject.class);
        JsonObject output = jsonObject.getAsJsonObject("output");
        if (output.get("data") == null) {
            errorMsg = "出现错误！data值为null，可能没有此角色，请使用角色名生成（如 \"白洲梓\" 请说 \"梓\")";
            Log.errorWithoutE("data值为null，可能为无此角色");
            return;
        }
        JsonElement data = output.get("data");
        if (!data.isJsonArray()) {
            errorMsg = "出现错误！请找Travellerr Sensei查看后台哦";
        } else {
            // 获取数组中第二个元素对应的JsonObject
            JsonObject secondElementObject = data.getAsJsonArray().get(1).getAsJsonObject();

            // 获取name字段对应的值
            String filePath = secondElementObject.get("name").getAsString();

            downloadVoice.downloadFromUrl(filePath, url);
        }
    }

    @Override
    public void onOpen(ServerHandshake handShakeData) {
        //System.out.println("Connected to server: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        //System.out.println("Received message from server: " + message);
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 处理接收到的消息
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        //System.out.println("Disconnected from server: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error occurred: " + ex.getMessage());
    }

    public String getNextMessage() throws InterruptedException {
        return messageQueue.take();
    }
}
