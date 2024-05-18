# 蔚蓝档案额外功能插件
## 指令
目前共四个指令

已接入 _**Mirai Console**_ 指令系统

|            指令             |        功能        |  作用域   |
|:-------------------------:|:----------------:|:------:|
|          `/今日运势`          |      查看今日运势      | **所有** |
|           `/监控`           |    查看服务器资源占用     | **所有** |
|          `/随机柴郡`          |    获取随机柴郡表情包     | **所有** |
|     `/语音生成 [角色] [文本]`     |    调用蔚蓝档案语音生成    | **所有** |
| `/语音生成 [角色] [文本] <中/日/英>` | 调用蔚蓝档案语音生成，自定义语言 | **所有** |

## 权限节点

指令系统权限节点如下 (所有节点使用LuckPerm权限查看器获取，可能有偏差）

| **指令** |                    **权限节点**                    |
|:------:|:----------------------------------------------:|
| `今日运势` |     `cn.travellerr.aronabot.command.jrys`      |
| `随机柴郡` | `cn.travellerr.aronabot.command.random-chaiq`  |
|  `监控`  | `cn.travellerr.aronabot.command.securityimage` |
| `语音生成` |   `cn.travellerr.aronabot.command.voice-gen`   |

## 配置

```yaml
# 本地字体目录,以mcl为主目录填写相对路径
useLocalFont: ''

# 是否启用语音合成
useVoice: true

# 语音合成模型地址
url: 'travellerr11-ba-voice-models.hf.space'

# 是否使用SilkConverter
useSilk: false

# ffmpeg地址,以mcl为主目录填写相对路径,若启用语音合成且不使用SilkConverter则必须填写ffmpeg路径
ffmpegPath: ''


# 启用群聊退出提示
useGroupLeave: true
# 启用龙王转移提示
useGroupDragon: true
```

## 声明

**群聊成员退出提示** 与 **启用龙王转移提示** 摘取于`LaoLittle tsudzuki`的项目[AutoGroup](https://github.com/LaoLittle/AutoGroup)
如果有问题请联系我进行删除

## 版本

`Version = 1.0.5`
