![](https://socialify.git.ci/Travellerrr/AronaBot/image?description=1&font=Raleway&forks=1&issues=1&language=1&name=1&owner=1&pattern=Circuit%20Board&pulls=1&stargazers=1&theme=Auto)

# 蔚蓝档案额外功能插件
## 指令
目前共四个指令

已接入 _**Mirai Console**_ 指令系统

|            指令             |        功能        |  作用域   |
|:-------------------------:|:----------------:|:------:|
|          `/今日运势`          |      查看今日运势      | **所有** |
|          `/今日人品`          |     查看今日人品值      | **所有** |
|           `/监控`           |    查看服务器资源占用     | **所有** |
|          `/随机柴郡`          |    获取随机柴郡表情包     | **所有** |
|     `/生成后缀 [名称] [后缀]`     | 使用unicode码生成名字后缀 | **所有** |
|     `/语音生成 [角色] [文本]`     |    调用蔚蓝档案语音生成    | **所有** |
| `/语音生成 [角色] [文本] <中/日/英>` | 调用蔚蓝档案语音生成，自定义语言 | **所有** |
|    `/aronabot reload`     |      重载配置文件      | **所有** |

## 权限节点

指令系统权限节点如下

| **指令** |                    **权限节点**                    |
|:------:|:----------------------------------------------:|
| `今日运势` |     `cn.travellerr.aronabot:command.jrys`      |
| `今日人品` |     `cn.travellerr.aronabot:command.jrrp`      |
|  `监控`  | `cn.travellerr.aronabot:command.securityimage` |
| `随机柴郡` | `cn.travellerr.aronabot:command.random-chaiq`  |
| `生成后缀` | `cn.travellerr.aronabot:command.generatename`  |
| `语音生成` |   `cn.travellerr.aronabot:command.voice-gen`   |
| `重载配置` |   `cn.travellerr.aronabot:command.aronabot`    |

## 配置

```yaml
# 是否启用文字输出运势
isText: false

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

```

---

## 关于 `/今日人品` 指令

该指令与 `/今日运势` 指令 使用同一数据库，所以获取到的人品值应该是和运势挂钩的

如果当天已经使用过了 `/今日运势` 指令，那么 `/今日人品` 不会重新生成，而是直接获取之前的运势id，向101取余计算人品值
例如:

| ID | QQ        | FortuneID | Date                |
|----|-----------|-----------|---------------------|
| 1  | 123456789 | 348       | 2024-05-14 21:51:30 |
| 2  | 114514123 | 200       | 2024-03-23 18:13:27 |
| 3  | 191981011 | 126       | 2024-07-02 19:36:03 |

分别将计算为

| QQ        | jrrpValue |
|-----------|-----------|
| 123456789 | 48        |
| 114514123 | 100       |
| 191981011 | 26        |

<br>

~~但是可能因为储存运势的文件没有经过排序，看起来没关系)~~

您可以通过将 `jrys.json` 按程度进行排序实现**真正的** `运势与人品关联

如果您做到了这点，希望能够将该文件PR到本仓库中

---

## 版本

`Version = 1.1.1`

## 推广
[我做的可自定义的好感度插件](https://github.com/Travellerrr/Favorability/)

---

[![Stargazers over time](https://starchart.cc/Travellerrr/AronaBot.svg?variant=adaptive)](https://starchart.cc/Travellerrr/AronaBot)
