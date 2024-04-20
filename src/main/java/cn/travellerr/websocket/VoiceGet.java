package cn.travellerr.websocket;

import net.mamoe.mirai.contact.AudioSupported;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;

import static cn.travellerr.AronaBot.ffmpeg;
import static cn.travellerr.websocket.VoiceWebSocketClient.errorMsg;
import static cn.travellerr.websocket.downloadVoice.tempId;

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

    public static void make(MessageEvent event, boolean lang, String url, boolean useSilk) {
        MessageChain message = event.getMessage();
        User user = event.getSender();
        String code = message.serializeToMiraiCode();
        Contact subject = event.getSubject();

        String[] s = code.split("说 ");
        String character = s[0];
        character = character.substring(1);
        String msg = s[1];
        String language = null;
        if (lang) {
            String[] langMsg = msg.split(" ");
            msg = langMsg[0];
            language = langMsg[1];
            if (language.contains("日")) language = "日本語";
            else if (language.contains("中")) language = "简体中文";
            else if (language.contains("英")) language = "English";

        }

        try {
            VoiceWebSocketClient.webSocket(character, msg, language, url);
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