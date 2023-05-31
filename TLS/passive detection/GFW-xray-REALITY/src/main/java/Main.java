import com.google.gson.Gson;
import com.google.gson.JsonObject;
import inet.ipaddr.IPAddressString;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        HashMap<String, String> i4 = new HashMap<>();
        HashMap<String, String> i6 = new HashMap<>();
        HashMap<String, String> dj = new HashMap<>();
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        executorService.execute(main.new A1(i4));
        executorService.execute(main.new A2(i6));
        executorService.execute(main.new A3(dj));
        executorService.shutdown();
        while (true){
            if (executorService.isTerminated()){
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ExecutorService executorService1 = Executors.newVirtualThreadPerTaskExecutor();
        for (HashMap.Entry<String, String> entry : dj.entrySet()) {
            executorService1.execute(main.new B1(entry.getKey(),entry.getValue(),i4,i6));
        }
        executorService1.shutdown();
        while (true){
            if (executorService1.isTerminated()){
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void hd(){

    }
    class A1 implements Runnable{
        HashMap<String, String> i4;
        public A1(HashMap i4){
            this.i4=i4;
        }
        @Override
        public void run() {
            InputStreamReader input = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("GeoLite2-ASN-Blocks-IPv4.csv"));
            CSVParser csvParser = null;

            try {
//                csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(input);
                csvParser = CSVFormat.EXCEL.builder().setHeader().build().parse(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (CSVRecord record : csvParser) {
                String s1 = record.get("network");
                String s2 = record.get("autonomous_system_number");
                i4.put(s1,s2);
            }
            try {
                csvParser.close();
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class A2 implements Runnable{
        HashMap<String, String> i6;
        public A2(HashMap i6){
            this.i6=i6;
        }
        @Override
        public void run() {
            InputStreamReader input = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("GeoLite2-ASN-Blocks-IPv6.csv"));
            CSVParser csvParser = null;

            try {
//                csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(input);
                csvParser = CSVFormat.EXCEL.builder().setHeader().build().parse(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (CSVRecord record : csvParser) {
                String s1 = record.get("network");
                String s2 = record.get("autonomous_system_number");
                i6.put(s1,s2);
            }
            try {
                csvParser.close();
                input.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class A3 implements Runnable{
        HashMap<String, String> dj;
        public A3(HashMap dj){
            this.dj=dj;
        }
        @Override
        public void run() {
            FileInputStream fileInputStream=null;
            byte[] bytes;
            try {
                fileInputStream = new FileInputStream(System.getProperty("user.dir") + "/data.json");
                bytes = fileInputStream.readAllBytes();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                if (fileInputStream!=null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson("{\"jsonlist\":" + new String(bytes) + "}", JsonObject.class);
            for (int i = 0; i < jsonObject.get("jsonlist").getAsJsonArray().size(); i++) {
                String ip=null;
                String ho=null;
                try {
                    ip=jsonObject.get("jsonlist").getAsJsonArray().get(i).getAsJsonObject().get("_source").getAsJsonObject().get("layers").getAsJsonObject().get("ip").getAsJsonObject().get("ip.dst").toString().replaceAll("\"","");
                }catch (Exception e){
                    try {
                        ip=jsonObject.get("jsonlist").getAsJsonArray().get(i).getAsJsonObject().get("_source").getAsJsonObject().get("layers").getAsJsonObject().get("ipv6").getAsJsonObject().get("ipv6.dst").toString().replaceAll("\"","");
                    }catch (Exception e1){
                        continue;
                    }
                }
                try {
                    ho=jsonObject.get("jsonlist").getAsJsonArray().get(i).getAsJsonObject().get("_source").getAsJsonObject().get("layers").getAsJsonObject().get("tls").getAsJsonObject().get("tls.record").toString().replaceAll("\"","");
                    String[] split = ho.split("tls.handshake.extensions_server_name:");
                    String[] split1 = split[1].split("}");
                    ho=split1[0];
                }catch (Exception e){
//                    System.out.println(e);
                    continue;
                }
                if (ip!=null&&ho!=null&&ip.length()>0&&ho.length()>0){
                    dj.put(ip,ho);
                }
            }
        }
    }

    class B1 implements Runnable{
        String k;
        String v;
        HashMap i4;
        HashMap i6;
        public B1(String k,String v,HashMap i4,HashMap i6){
            this.k=k;
            this.v=v;
            this.i4=i4;
            this.i6=i6;
        }
        @Override
        public void run() {
            InetAddress[] addresses = null;
            String v_ip=null;
            try {
                addresses = InetAddress.getAllByName(v);
                for (InetAddress address : addresses) {
                    if (address instanceof java.net.Inet6Address&&k.contains(":")) {
                        v_ip=address.getHostAddress();
                        break;
                    } else if (address instanceof java.net.Inet4Address&&k.contains(".")){
                        v_ip=address.getHostAddress();
                        break;
                    }
                }
                if (v_ip==null)
                v_ip=addresses[0].getHostAddress();
            } catch (UnknownHostException e) {
                System.out.println(e);
                return;
            }
            int[] ints1 = new int[1];
            int[] ints2 = new int[1];
            ints1[0]=0;
            ints2[0]=0;
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
            executorService.execute(this.new C1(k,ints1,i4,i6));
            executorService.execute(this.new C1(v_ip,ints2,i4,i6));
            executorService.shutdown();
            while (true){
                if (executorService.isTerminated()){
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ints1[0]==0){
                System.out.println("ip "+k+" is local ip? or other?");
                return;
            }
            if (ints2[0]==0){
                System.out.println(v+" is wrong host name?");
                return;
            }
            if (ints1[0]!=ints2[0]){
                System.out.println("ip "+k+" xray-REALITY, "+v);
            }
        }

        class C1 implements Runnable{
            String ip;
            int[] i1;
            HashMap<String ,String > i4;
            HashMap<String ,String > i6;
            public C1(String ip,int[] i1,HashMap i4,HashMap i6){
                this.ip=ip;
                this.i1=i1;
                this.i4=i4;
                this.i6=i6;
            }
            @Override
            public void run() {
                if (ip.contains(".")){
                    for (String key : i4.keySet()) {
                        boolean containing = new IPAddressString(key).getAddress().contains(
                                new IPAddressString(ip).getAddress());
                        if (containing){
                            i1[0]= Integer.parseInt(i4.get(key));
//                            System.out.println(ip+"asn"+i1[0]);
                            return;
                        }
                    }
                }else {
                    for (String key : i6.keySet()) {
                        boolean containing = new IPAddressString(key).getAddress().contains(
                                new IPAddressString(ip).getAddress());
                        if (containing){
                            i1[0]= Integer.parseInt(i6.get(key));
//                            System.out.println(ip+"asn"+i1[0]);
                            return;
                        }
                    }
                }
            }
        }
    }
}
