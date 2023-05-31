Xray-REALITY 基於旁側的方式識別

識別原理是基於這個[https://github.com/XTLS/REALITY](https://github.com/XTLS/REALITY)

需要jdk21+才能運作

我截取了5分鐘的上網資料，誤報率大約是4.7%，對同樣的資料再次運行判定誤報率為5.9%，第3次對同樣的資料進行判定誤報率為6.5%
這種東西肯定沒法部署，如果是對重要網站執行白名單，應該是可以降低誤報率

使用方式
```
1.你需要用wireshark獲取資料
2.在wireshark設定過濾，tls.handshake.type==1&&!(udp.port==443)
上面的意思是只獲取client hello，并且過濾掉quic
3.然後在wireshark把資料導出成json，並改名成data.json
4.截取5分鐘以上的資料
5.mvn package
6.然後把編譯出來的jar和data.json放在同一個資料夾内
7.java -jar ./gfw.jar

如果顯示ip x.x.x.x xray-REALITY 那麽就代表你可能用了xray-REALITY
```