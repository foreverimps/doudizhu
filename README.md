# 斗地主

本程序是想打造手机端+浏览器（支持webSocket）+桌面软件为一体的服务端，直白点说就是手机端用户、浏览器用户
、桌面软件用户可以在同一个房间进行斗地主。
手机端、桌面软件是通过socket长连进行数据交互，浏览器是通过webSocket长连进行数据交互。

#技术框架

springMVC、mybatis、webSocket、mina、jms、mongoDB、memcache

#使用

SocketHandler类用来接收和返回客户端信息的处理类
WebGameSocketHandler类功能跟SocketHandler功能一样，不过它只处理浏览器相关信息。

