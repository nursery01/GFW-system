Xray-REALITY 基於旁側的方式識別

識別原理是基於這個[https://github.com/XTLS/REALITY](https://github.com/XTLS/REALITY)

它匹配的是asn進行碰撞

需要jdk21+才能運作

我截取了5分鐘的上網資料，誤報率大約是4.7%，對同樣的資料再次運行判定誤報率為5.9%，第3次對同樣的資料進行判定誤報率為6.5%，第4次和第5次是5%左右

這種東西肯定沒法部署，如果是對重要網站執行白名單，應該是可以降低誤報率

更新1

我第2天再對昨天同一份舊資料用同一個dns供應商進行測試是10.7%誤報率，然後更換dns并且刷新dns快取后是9.5%誤報率，更換dns供應商沒發現會導致誤報率大幅波動，倒是時間過久了會導致誤報率上漲

可能是服務ip換了，而且昨天和今天剛好是月底31日和月初1日

誤報的網域大多數都是cdn或者api

更新2
~~結果我自己先引了~~ 啊，一不小心就。。。一不小心就。。。
RPRX的要求[https://github.com/XTLS/Xray-core/issues/2230#issuecomment-1598256976](https://github.com/XTLS/Xray-core/issues/2230#issuecomment-1598256976)

我的測試是不完善，并且當時的測試環境沒有dns污染，你們有興趣自己去測試，也許比我高或者比我低

有沒有想説什麽重要的事情？重要的事情就是精心挑選dst

我也沒有打算誤導新手，我倒是覺得這個東西可以讓他們知道不要拿AWS複製微軟或者拿Azure複製亞馬遜或者拿甲骨文複製蘋果


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

防止被這種旁側識別的解決方案就是

1.相近的ip(其實就是R神説過的話)

2.偷自己的網站(100%無法通過這種旁側方法識別，因爲就是自己的)
