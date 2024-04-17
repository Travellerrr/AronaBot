package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object config : AutoSavePluginConfig("Config") {
    @ValueDescription("主人\n")
    var owner: Long by value()

    @ValueDescription("主机器人\n")
    var bot: Long by value()

    @ValueDescription("监控授权\n")
    var user: List<Long> by value()

    @ValueDescription("指令前缀\n")
    var prefix: String by value("/")
}