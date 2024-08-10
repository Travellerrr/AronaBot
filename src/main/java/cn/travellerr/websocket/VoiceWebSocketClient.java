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
import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VoiceWebSocketClient extends WebSocketClient {
    //static String msg;
    protected static String errorMsg = null;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public VoiceWebSocketClient(URI uri) {
        super(uri);
    }

    public static void webSocket(String character, String msg, String lang, String url) throws InterruptedException, URISyntaxException {
        errorMsg = null;
        String serverURI = "wss://" + url + "/queue/join"; // 连接的服务器 URI
        VoiceWebSocketClient client = new VoiceWebSocketClient(new URI(serverURI));
        client.connect(); // 连接 WebSocket 服务器
        //System.out.println("Connecting to server...");
        while (!client.getReadyState().equals(ReadyState.OPEN)) {
            Thread.sleep(1000); // 等待连接成功
            //System.out.println("Connecting...");
        }
        //System.out.println("Connected.");

        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        String hash = stringBuilder.toString();
        // 发送消息
        String message = "{\"fn_index\":2,\"session_hash\":\"" + hash + "\"}";
        client.send(message);
        String voiceUrl;
        try {
            String receivedMessage;
            do {
                receivedMessage = client.getNextMessage();
                if (receivedMessage.contains("send_data")) {
                    message = "{\"data\":[\"" + msg + "\",\"" + character + "\",\"" + lang + "\",0.6,0.668,1,false],\"event_data\":null,\"fn_index\":2,\"session_hash\":\"" + hash + "\"}";
                    client.send(message);
                }
            } while (!receivedMessage.contains("process_completed"));
            voiceUrl = receivedMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.close(); // 关闭 WebSocket 连接
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(voiceUrl, JsonObject.class);
        Log.debug(String.valueOf(jsonObject));
        JsonObject output = jsonObject.getAsJsonObject("output");
        Log.debug(String.valueOf(output));
        JsonElement data = output.get("data");
        if (data == null) {
            errorMsg = "出现错误！data值为null，可能没有此角色，请使用角色名生成（如 \"白洲梓\" 请说 \"梓\")";
            return;
        }
        if (!data.isJsonArray()) {
            errorMsg = "出现错误！请找主人查看后台哦";
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
        Log.error("Error occurred: " + ex.getMessage());
    }

    public String getNextMessage() throws InterruptedException {
        return messageQueue.take();
    }
}
