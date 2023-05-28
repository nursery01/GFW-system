import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        ArrayList rf = main.rf();
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (int i = 0; i < rf.size(); i++) {
            executorService.execute(main.new A1(rf.get(i).toString(),i+1,concurrentHashMap));
        }
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
        boolean b=false;
        for (int i = 1; i < concurrentHashMap.size()+1; i++) {
            String o = (String) concurrentHashMap.get("data" + i);
            if (o.endsWith("passed")){
                b=true;
            }
            System.out.println(o);
        }
        if (!b){
            System.out.println("vpn?vpn?vpn?");
        }
    }

    private ArrayList rf(){
        ArrayList<Object> objects = new ArrayList<>();
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
            String s=null;
            try {
                s = jsonObject.get("jsonlist").getAsJsonArray().get(i).getAsJsonObject().get("_source").getAsJsonObject().get("layers").getAsJsonObject().get("tcp").getAsJsonObject().get("tcp.payload").toString().replaceAll("\"","");
            }catch (NullPointerException e){
                try {
                    s = jsonObject.get("jsonlist").getAsJsonArray().get(i).getAsJsonObject().get("_source").getAsJsonObject().get("layers").getAsJsonObject().get("data").getAsJsonObject().get("data.data").toString().replaceAll("\"","");
                }catch (NullPointerException e1){
                    continue;
                }
            }
            if (s!=null&&s.length()>0){
            objects.add(s);
        }
        }
        return objects;
    }

    class A1 implements Runnable{
        String s;
        int i;

        ConcurrentHashMap c;
        public A1(String s,int i,ConcurrentHashMap c){
            this.s=s;
            this.i=i;
            this.c=c;
        }

        @Override
        public void run() {
            CopyOnWriteArrayList<Boolean> booleans = new CopyOnWriteArrayList<>();
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
            executorService.execute(new B1(s,booleans));
            executorService.execute(new B2(s,booleans));
            executorService.execute(new B3(s,booleans));
            executorService.execute(new B4(s,booleans));
            executorService.execute(new B5(s,booleans));
            executorService.shutdown();
            while (true){
                if (executorService.isTerminated()){
                    if (booleans.size()!=0){
                        c.put("data"+i,"data "+i+" passed");
                    }else {
                        c.put("data"+i,"data "+i+" unknown or full encryption");
                    }
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        class B1 implements Runnable{
            String s;
            CopyOnWriteArrayList b;

            public B1(String s,CopyOnWriteArrayList b){
                this.s=s;
                this.b=b;
            }
            @Override
            public void run() {
                boolean[] bo=new boolean[6];
                String[] split = s.split(":");
                if (split.length<6){
                    return;
                }
                for (int i = 0; i < 6; i++) {
                    int i1 = Integer.parseInt(split[i], 16);
                    if (i1>=32&&i1<=126){
                        bo[i]=true;
                    }else {
                        bo[i]=false;
                    }
                }
                if (bo[0]&&bo[1]&&bo[2]&&bo[3]&&bo[4]&&bo[5]){
                    b.add(true);
                }
            }
        }
        class B2 implements Runnable{
            String s;
            CopyOnWriteArrayList b;

            public B2(String s,CopyOnWriteArrayList b){
                this.s=s;
                this.b=b;
            }
            @Override
            public void run() {
                String[] split = s.split(":");
                if (split.length<2){
                    return;
                }
                int i=0;
                for (String s1 : split) {
                    int i1 = Integer.parseInt(s1, 16);
                    if (i1>=32&&i1<=126){
                        i++;
                    }
                }
                if (i>split.length){
                    b.add(true);
                }
            }
        }
        class B3 implements Runnable{
            String s;
            CopyOnWriteArrayList b;

            public B3(String s,CopyOnWriteArrayList b){
                this.s=s;
                this.b=b;
            }
            @Override
            public void run() {
                String[] split = s.split(":");
                if (split.length<21){
                    return;
                }
                int i=0;
                for (String s1 : split){
                    if (i>20){
                        break;
                    }
                    int i1 = Integer.parseInt(s1, 16);
                    if (i1>=32&&i1<=126){
                        i++;
                    }else {
                        i=0;
                    }
                }
                if (i>20){
                    b.add(true);
                }
            }
        }
        class B4 implements Runnable{
            String s;
            CopyOnWriteArrayList b;

            public B4(String s,CopyOnWriteArrayList b){
                this.s=s;
                this.b=b;
            }
            @Override
            public void run() {
                String[] split = s.split(":");
                if (split.length<1){
                    return;
                }
                int i=0;
                for (String s1 : split) {
                    int i1 = Integer.parseInt(s1, 16);
                    String binaryString = Integer.toBinaryString(i1);
                    for (int j = 0; j < binaryString.length(); j++) {
                        if (binaryString.charAt(j)=='1'){
                            i++;
                        }
                    }
                }
                double d=1.0*i/split.length;
                if (d>4.6||d<3.4){
                    b.add(true);
                }
            }
        }
        class B5 implements Runnable{
            String s;
            CopyOnWriteArrayList b;

            public B5(String s,CopyOnWriteArrayList b){
                this.s=s;
                this.b=b;
            }
            @Override
            public void run() {
                String[] split = s.split(":");
                if (split.length<3){
                    return;
                }
                if ((split[0]=="16"||split[0]=="17")&&split[1]=="03"){
                    int i = Integer.parseInt(split[2]);
                    if (i>=0&&i<=9){
                        b.add(true);
                        return;
                    }
                }
                if (split.length<4){
                    return;
                }
                if (split[0]=="47"&&split[1]=="45"&&split[2]=="54"&&split[3]=="20"){
                    b.add(true);
                    return;
                }
                if (split[0]=="50"&&split[1]=="55"&&split[2]=="54"&&split[3]=="20"){
                    b.add(true);
                    return;
                }
                if (split.length<5){
                    return;
                }
                if (split[0]=="50"&&split[1]=="4f"&&split[2]=="53"&&split[3]=="54"&&split[4]=="20"){
                    b.add(true);
                    return;
                }
                if (split[0]=="48"&&split[1]=="45"&&split[2]=="41"&&split[3]=="44"&&split[4]=="20"){
                    b.add(true);
                    return;
                }
            }
        }
    }
}
