package cn.travellerr.command

import cn.travellerr.AronaBot
import cn.travellerr.BlueArchive.Jrrp
import cn.travellerr.BlueArchive.Jrys
import cn.travellerr.config.Config.url
import cn.travellerr.config.Config.useSilk
import cn.travellerr.tools.Api
import cn.travellerr.tools.Log
import cn.travellerr.tools.SecurityNew
import cn.travellerr.websocket.VoiceGet
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.SingleMessage

object GetJrys : SimpleCommand(AronaBot.INSTANCE, "jrys", "今日运势", description = "获取今日运势信息") {
    @Handler
    fun useJrys(sender: CommandSender) {
        Log.info("运势指令")
        val subject: Contact? = sender.subject
        val user: User? = sender.user
        Jrys.info(subject, user)
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
            AronaBot.INSTANCE.reloadPluginConfig(AronaBot.blackList)
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
        Api.chaiq(subject, user)
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
        MiraiConsole
    }

    @Handler
    suspend fun useVoiceError(sender: CommandContext) {
        val subject: Contact? = sender.sender.subject
        val originMsg: SingleMessage = sender.originalMessage[1]
        val prefix = originMsg.toString().split(" ")[0]
        if (subject is Contact) {
            subject.sendMessage("请使用 \"$prefix [角色名称] [文本] <中/日/英>\"生成，不要加括号")
        }
    }
}

object GenerateUnicodeName : SimpleCommand(AronaBot.INSTANCE, "generateName", "生成名称后缀", "生成名称", "生成后缀") {
    @Handler
    suspend fun generateName(sender: CommandContext, name: String, suffix: String) {

        val generated: String = "$name \u2060\u202D\u2067" + suffix.substring(
            0,
            suffix.length
        ) + "\u2067\u202D"
        val subject: Contact? = sender.sender.subject
        if (subject is Contact) {
            subject.sendMessage(generated)
        }
    }

    @Handler
    suspend fun generateNameError(sender: CommandContext) {
        val subject: Contact? = sender.sender.subject
        val originMsg: SingleMessage = sender.originalMessage[1]
        val prefix = originMsg.toString().split(" ")[0]
        if (subject is Contact) {
            subject.sendMessage("请使用 \"$prefix [名称] [后缀]\"生成")
        }
    }
}

object GetJrrp : SimpleCommand(AronaBot.INSTANCE, "jrrp", "今日人品") {
    @Handler
    fun getJrrp(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.user

        Jrrp.info(subject, user)
    }
}


