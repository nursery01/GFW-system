gfw2021(ver1.0)，可以用來檢測Shadowsocks、vmess等(外層沒有加TLS)

基於這篇製作[https://gfw.report/publications/usenixsecurity23/zh/](https://gfw.report/publications/usenixsecurity23/zh/)

需要jdk21才能運作

```
1.你需要用wireshark獲取資料
2.在wireshark設定過濾，tcp.port == SERVER PORT && (ip.dst == SERVER IP || ipv6.dst == SERVER IP)
3.然後在wireshark把資料導出成json，並改名成data.json
4.截取5分鐘以上的資料
5.mvn package
6.然後把編譯出來的jar和data.json放在同一個資料夾内
7.java -jar ./gfw.jar

如果顯示為unknown or full encryption，則代表該條沒有通過規則
如果結尾顯示vpn?vpn?vpn? 那麽就代表你可能用了shadowsocks或者vmess
```