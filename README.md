# 中文版
# 眼
前言
小心你的背后！Minecraft作为一款全球顶尖的沙盒游戏具有丰富的创造性和可玩性与趣味性，其中生存模式吸引了许多玩家由此入门，我在2016年左右也加入了MC玩家的阵营。但是胆小的我面对黑夜和深不见底的海底与伸手不见五指的漆黑矿洞时停住了探索的步伐：下一个转角可能就僵尸骑脸、下一个回头可能就和苦力怕一起爆炸了，终于在这种想玩但是不敢玩的情况下我逐渐越来越少的打开mc游戏。

引言
我也想过解决办法，怪物娘化模组必然是首选，可是随着版本不断的更新，会导致越来越多的敌对生物没有被修改模型。我还尝试过通过指令保护我家周围的一片地，一旦有怪物刷新就被清除，可是这也不是长久之计。同时若开和平模式，虽然没了怪物的偷袭，但是曾听闻him的故事，漆黑的矿洞与夜晚仍然绊住了我探索的步伐。最后我把希望寄托于模组上，可惜找了许久也没有满足我需求的一款模组。

介绍
但是不用担心！现在已经有这款模组帮你实时监控附近的实体了！我效仿minecraft源码的SubTitleHud（屏幕右下角声音提示）代码与思路，实现了屏幕中央下方的实体信息HUD显示区域，第一行能够显示敌对实体与环境实体，第二行显示其余实体（生物、凋落物等），它们不会占用你屏幕的很多区域，并且会像SubTitleHud那样在一段时间检测不到实体后淡出。
同时！不光能够显示实体信息，还能够显示在三维空间内距离你的距离（格数）与左右方位（PS：感谢MC源码里的SubTitleHud部分让我有实现的思路（指复制粘贴）），这样就能够率先获取到实体相对于你的大致方位。为了在知道方位却仍然无法发现实体保持高度警惕情绪的时候被忽然出现的什么东西搞得恐惧超级加倍，【眼】还会高亮实体，让你快速定位。

总结功能：
1. 在【眼】的设置界面设置检测半径。
2. 在指定半径内实时在屏幕中下方hud显示检测到的最近实体信息与方位。
3. 实体高亮辅助玩家快速定位。

待改进（2021-2-28 19：30：03）：
1. 目前只能在检测半径内实体被更新（比如实体抬头、移动等）时检测到。
2. 出于未知原因文本无法加载语言json文件中的键值对，所以暂且写死。
3. 检测范围上限不可调、更多的自定义设置未实现。

预览截图：
![Alt text](https://gitee.com/a_box/other/raw/master/screenshot.png "预览截图")

演示视频：
暂无制作

# English version(machine translation)
# Eye

preface
Watch your back! Minecraft is one of the world's top sandbox games, which is rich in creativity, playability and fun. Survival mode attracts many players to get started. I joined MC players around 2016. But when I face the dark night, the bottomless sea floor and the dark mine cave, I stop exploring: the next corner may be zombie riding face, the next turn may explode together with coolie. Finally, in this situation that I want to play but dare not play, I gradually turn on the MC game less and less.

introduction
I've also thought about the solution. The monster Niang module must be the first choice. However, with the continuous update of the version, more and more hostile creatures will not be modified. I have also tried to protect a piece of land around my home by command. Once there is a monster refresh, it will be cleared, but this is not a long-term solution. At the same time, in Ruo Kai peace mode, although there was no surprise attack from monsters, I heard the story of him, and the dark mine and night still hindered my exploration. Finally, I put my hope on the module, but I didn't find a module to meet my needs after a long time.

introduce
But don't worry! Now there is this module to help you monitor the nearby entities in real time! Following the code and idea of subtitlehud (voice prompt in the lower right corner of the screen) of minecraft source code, I realized the HUD display area of entity information in the lower center of the screen. The first line can display hostile entities and environmental entities, and the second line can display other entities (organisms, litter, etc.), which will not occupy many areas of your screen, and will be checked in a period of time like subtitlehud Fade out when no entity is detected.

At the same time! It can not only display entity information, but also display the distance (grid number) and left and right directions from you in three-dimensional space (PS: thanks for the subtitlehud part of MC source code, which gives me the idea of implementation (copy and paste)), so that I can get the approximate orientation of the entity relative to you first. When you know the location but still can't find the entity, when you are on high alert, you will be scared by something that suddenly appears, and the [eye] will highlight the entity, so that you can quickly locate it.

Summary function:
1. Set the detection radius in the eye setting interface.
2. Within the specified radius, the HUD in the lower part of the screen displays the detected nearest entity information and orientation in real time.
3. Entity highlight helps players locate quickly.

To be improved (2021-2-28 19:30:03)
1. At present, it can only be detected when the entity within the detection radius is updated (such as entity head up, moving, etc.).
2. Due to unknown reasons, the text cannot load the key value pair in the language JSON file, so it is temporarily dead.
3. The upper limit of detection range is not adjustable, and more custom settings are not implemented.

Preview screenshot:
![Alt text](https://gitee.com/a_box/other/raw/master/screenshot.png "Preview screenshot")

Demo video:
No production
