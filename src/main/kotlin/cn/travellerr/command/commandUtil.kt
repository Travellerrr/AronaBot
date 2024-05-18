package cn.travellerr.command

import cn.travellerr.AronaBot
import cn.travellerr.BlueArchive.jrys
import cn.travellerr.config.Config.url
import cn.travellerr.config.Config.useSilk
import cn.travellerr.tools.Log
import cn.travellerr.tools.SecurityNew
import cn.travellerr.tools.api
import cn.travellerr.websocket.VoiceGet
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MessageChain

object GetJrys : SimpleCommand(AronaBot.INSTANCE, "jrys", "今日运势", description = "获取今日运势信息") {
    @Handler
    fun useJrys(sender: CommandSender) {
        Log.info("运势指令")
        val subject: Contact? = sender.subject
        val user: User? = sender.user
        jrys.info(subject, user)
    }
}

object GetSecurity : SimpleCommand(AronaBot.INSTANCE, "securityImage", "监控", description = "查看机器人状态") {
    @Handler
    fun useSecurity(sender: CommandSender) {
        Log.info("监控指令")
        val subject: Contact? = sender.subject
        val user: User? = sender.user
        SecurityNew.Security(subject, user)
    }

}

object ReloadConfig : SimpleCommand(AronaBot.INSTANCE, "AronaBot", description = "重载配置") {
    @Handler
    suspend fun reload(sender: CommandSender, msg: String) {
        if (msg == "reload") {
            AronaBot.INSTANCE.reloadPluginConfig(AronaBot.config)
            sender.sendMessage("重载已完成")
        }
    }
}

object RandomChaiq : SimpleCommand(AronaBot.INSTANCE, "random-chaiq", "随机柴郡", description = "获取随机柴郡表情包") {
    @Handler
    fun getChaiq(sender: CommandSender) {
        val subject: Contact? = sender.subject
        val user: User? = sender.user
        Log.info("表情包指令")
        api.chaiq(subject, user)
    }
}

object GetVoice : SimpleCommand(AronaBot.INSTANCE, "voice-gen", "语音生成") {
    @Handler
    fun useVoice(sender: CommandContext, vararg args: String) {

        Log.info("语音生成")

        val subject: Contact? = sender.sender.subject
        val user: User? = sender.sender.user
        val message: MessageChain = sender.originalMessage
        VoiceGet.make(subject, user, message, url, useSilk)
    }
}


