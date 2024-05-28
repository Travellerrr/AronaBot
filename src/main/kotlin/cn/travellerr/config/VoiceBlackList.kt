package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object VoiceBlackList : AutoSavePluginConfig("VoiceBlackList") {
    @ValueDescription("语音生成文字黑名单\n")
    val blackList by value(
        listOf(
            "傻逼",
            "你妈",
            "泥马",
            "你妈",
            "弱智",
            "二逼",
            "脑残",
            "没马",
            "没妈",
            "没木",
            "没母",
            "双亡"
        )
    )
}