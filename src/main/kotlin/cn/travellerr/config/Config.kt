package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("Config") {
    @ValueDescription("是否启用文字输出运势\n")
    var isText: Boolean by value(false)

    @ValueDescription("用户后缀\n")
    var suffix: String by value("Sensei")

    @ValueDescription("本地字体目录\n")
    var useLocalFont: String by value()

    @ValueDescription("是否启用语音合成\n")
    var useVoice: Boolean by value(true)

    @ValueDescription("语音合成模型地址\n")
    var url: String by value("travellerr11-ba-voice-models.hf.space")

    @ValueDescription("是否使用SilkConverter\n")
    var useSilk: Boolean by value(false)

    @ValueDescription("ffmpeg地址\n")
    var ffmpegPath: String by value()

    @ValueDescription("是否开启二次元语录回复")
    var isReply: Boolean by value(false)

    @ValueDescription("语录需要@机器人触发")
    var isAt: Boolean by value(true)

    @ValueDescription("是否开启R18语录")
    var isR18: Boolean by value(false)

    @ValueDescription("Debug模式")
    var debug: Boolean by value(false)


}