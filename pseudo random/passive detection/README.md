gfw2021，可以用來檢測Shadowsocks、vmess等

來自[https://gfw.report/publications/usenixsecurity23/zh/](https://gfw.report/publications/usenixsecurity23/zh/)

需要jdk21才能運作

```
1.你需要用wireshark獲取資料
2.在wireshark設定過濾，tcp.port == YOUR PORT && (ip.dst == SERVER IP || ipv6.dst == SERVER IP)
3.然後在wireshark把資料導出成json，並改名成data.json
2.mvn package
2.然後把編譯出來的jar和data.json放在同一個資料夾内
3.java -jar ./gfw.jar

如果顯示為unknown or full encryption，則代表它是沒有通過
如果結尾顯示vpn?vpn?vpn? 那麽你就很危險啦
```