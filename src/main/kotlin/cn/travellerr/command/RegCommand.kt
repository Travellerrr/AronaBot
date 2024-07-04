package cn.travellerr.command

import net.mamoe.mirai.console.command.CommandManager

object RegCommand {
    fun register() {
        CommandManager.registerCommand(GetJrys)
        CommandManager.registerCommand(GetSecurity)
        CommandManager.registerCommand(ReloadConfig)
        CommandManager.registerCommand(RandomChaiq)
        CommandManager.registerCommand(GetVoice)
        CommandManager.registerCommand(GenerateUnicodeName)
        CommandManager.registerCommand(GetJrrp)
    }
}