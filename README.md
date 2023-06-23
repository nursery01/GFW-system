# GFW-system

### 一款工具集，模仿GFW系統檢測是否使用了VPN

### ⚠️只能用於學習用途 <- (認真看清楚這行字)

這是根據網路上流傳已久的方法製作而成，不保證其正確性和可行性，不保證科學性

誤報率肯定有，這是無法解決的問題

目前做了

1.GFW2021(受影響:僞隨機加密代理)可以用來檢測Shadowsocks、vmess等[https://github.com/nursery01/GFW-system/tree/main/pseudo%20random/passive%20detection](https://github.com/nursery01/GFW-system/tree/main/pseudo%20random/passive%20detection)

2.GFW-xray-REALITY(受影響:TLS代理)基於旁側的方式識別xray-REALITY[https://github.com/nursery01/GFW-system/tree/main/TLS/passive%20detection](https://github.com/nursery01/GFW-system/tree/main/TLS/passive%20detection)

3.時序特徵(未製作)(受影響:僞隨機加密代理、TLS代理)RPRX和yuhan提出時序特徵可以用來檢測TLS代理，首先我認可這個觀點，但是我要在這個基礎上補充一個猜想的是這種檢測方法誤報率可能會很高，因為api伺服器和cdn伺服器需要對上游進行請求，所以api伺服器和cdn伺服器可能會表現出類似TLS代理的時序特性

4.頻率檢測(未製作)(受影響:僞隨機加密代理、TLS代理)頻率檢測應對僞隨機加密代理和TLS代理都有效果，尤其是對TLS代理，TLS代理的頻率相對正常網站有差異，這個差異難以消除，包括發包頻率和握手頻率，這個是可以統計的

GPL Ver3.0
