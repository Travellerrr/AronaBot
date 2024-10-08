package cn.travellerr.websocket;

import cn.travellerr.tools.Log;
import net.mamoe.mirai.contact.AudioSupported;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;

import static cn.travellerr.AronaBot.blackList;
import static cn.travellerr.AronaBot.ffmpeg;
import static cn.travellerr.websocket.DownloadVoice.tempId;
import static cn.travellerr.websocket.VoiceWebSocketClient.errorMsg;

public class VoiceGet {
    static boolean verbose = true;

    public static void exeCmd(String... commandStr) {
        try {
            ProcessBuilder pb = new ProcessBuilder(commandStr);
            // Process p = Runtime.getRuntime().exec(commandStr);
            if (verbose)
                pb.inheritIO();
            pb.start().waitFor();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated(since = "加入MCL指令系统")
    public static void make(MessageEvent event, boolean lang, String url, boolean useSilk) {
        MessageChain message = event.getMessage();
        User user = event.getSender();
        String code = message.serializeToMiraiCode();
        Contact subject = event.getSubject();

        String[] s = code.split("说 ");
        String character = s[0];
        character = character.substring(1);
        StringBuilder msg = new StringBuilder(s[1]);
        String language = null;
        if (lang) {
            String[] langMsg = msg.toString().split(" ");
            StringJoiner msgJoiner = new StringJoiner(" ");
            for (int i = 0; i < langMsg.length - 1; i++) {
                msgJoiner.add(langMsg[i]);
            }
            msg = new StringBuilder(msgJoiner.toString());
            language = langMsg[langMsg.length - 1];
            if (language.contains("日")) language = "日本語";
            else if (language.contains("中")) language = "简体中文";
            else if (language.contains("英")) language = "English";

        }

        try {
            VoiceWebSocketClient.webSocket(character, msg.toString(), language, url);
            if (errorMsg != null) {
                subject.sendMessage(new At(user.getId()).plus(errorMsg));
                return;
            }
            File f = new File("temp/" + tempId + ".wav");
            if (!useSilk) {
                File f2 = new File("temp/amr" + tempId + ".amr");
                exeCmd(ffmpeg, "-i", f.getAbsolutePath(), "-ab", "23.85k", "-ar", "16000",
                        "-ac", "1", "-acodec", "amr_wb", "-fs", "1000000", "-y", f2.getAbsolutePath());
                ExternalResource resource = ExternalResource.create(f2);
                Audio audio = ((AudioSupported) subject).uploadAudio(resource);
                subject.sendMessage(audio);
                resource.close();
                f.delete();
                f2.delete();
            } else {
                ExternalResource resource = ExternalResource.create(f);
                Audio audio = ((AudioSupported) subject).uploadAudio(resource);
                subject.sendMessage(audio);
                resource.close();
                f.delete();
            }
            //f2.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void make(Contact subject, User user, MessageChain message, String url, boolean useSilk) {
        String code = message.serializeToMiraiCode();

        String[] s = code.split(" ");
        String character = s[1];
        StringBuilder msg = new StringBuilder();
        String language;

        language = s[s.length - 1];
        if (language.contains("日")) {
            language = "日本語";
            for (int i = 2; i < s.length - 1; i++) {
                msg.append(s[i]);
            }
        } else if (language.contains("英")) {
            language = "English";
            for (int i = 2; i < s.length - 1; i++) {
                msg.append(s[i]);
                msg.append(" ");
            }
        } else if (language.contains("中")) {
            language = "简体中文";
            for (int i = 2; i < s.length - 1; i++) {
                msg.append(s[i]);
                msg.append(" ");
            }
        } else {
            language = "简体中文";
            for (int i = 2; i < s.length; i++) {
                msg.append(s[i]);
            }
        }
        msg = new StringBuilder(msg.toString().replace("\\", ""));
        Log.info("Language: " + language);
        Log.info("Msg: " + msg);
        List<String> list = blackList.getBlackList();
        for (String blackStr : list) {
            if (msg.toString().contains(blackStr)) {
                subject.sendMessage(new At(user.getId()) + "\n请检查发言，内含违禁词！");
                return;
            }
        }

        try {
            VoiceWebSocketClient.webSocket(character, msg.toString(), language, url);
            if (errorMsg != null) {
                subject.sendMessage(new At(user.getId()).plus(errorMsg));
                return;
            }
            File f = new File("temp/" + tempId + ".wav");
            if (!useSilk) {
                File f2 = new File("temp/amr" + tempId + ".amr");
                exeCmd(ffmpeg, "-i", f.getAbsolutePath(), "-ab", "23.85k", "-ar", "16000",
                        "-ac", "1", "-acodec", "amr_wb", "-fs", "1000000", "-y", f2.getAbsolutePath());
                ExternalResource resource = ExternalResource.create(f2);
                Audio audio = ((AudioSupported) subject).uploadAudio(resource);
                subject.sendMessage(audio);
                resource.close();
                f.delete();
                f2.delete();
            } else {
                ExternalResource resource = ExternalResource.create(f);
                Audio audio = ((AudioSupported) subject).uploadAudio(resource);
                subject.sendMessage(audio);
                resource.close();
                f.delete();
            }
            //f2.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
