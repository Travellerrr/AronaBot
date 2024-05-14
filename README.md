# 蔚蓝档案额外功能插件
## 指令
目前共四个指令

可自定义指令前缀，在配置目录下修改`prefix`值即可

| 指令 | 功能 | 作用域 |
| :------------: | :------------: | :------------: |
| `/今日运势` | 查看今日运势 | **所有** |
| `/监控` | 查看服务器资源占用 | **所有** |
| `/xxx说 xxx` | 调用蔚蓝档案语音生成 | **所有**|
| `/xxx说 xxx [中文/日语/英文]` | 调用蔚蓝档案语音生成，自定义语言 | **所有** |

`/监控`指令需要在`config/cn.travellerr.AronaBot/Config.yml`下设置主人

`语音生成`指令需要在`config/cn.travellerr.AronaBot/Config.yml`设置是否启用

## 配置

```yaml
# 插件主人QQ号
owner: 0

# 主机器人，非必要配置
bot: 0

# 监控授权QQ号
user:
  - 123
  - 456

# 指令前缀
prefix: '/'

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
# 
# 启用群聊退出提示
useGroupLeave: true
# 启用龙王转移提示
useGroupDragon: true
```

## 声明

**群聊成员退出提示** 与 **启用龙王转移提示** 摘取于`LaoLittle tsudzuki`的项目[AutoGroup](https://github.com/LaoLittle/AutoGroup)
如果有问题请联系我进行删除

## 版本
`Version = 1.0.4`
