/**
 * Created by xuhuan on 2017/11/24.
 */
import java.io.Serializable;
import java.util.HashSet;

public class Connection implements Serializable {
    private int type; // 1私聊 0上下线更新 -1下线请求

    private HashSet<String> clients; // 存放选中的客户

    private String info;

    private String timer;

    private String name;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashSet<String> getClients() {
        return clients;
    }

    public void setClients(HashSet<String> clients) {
        this.clients = clients;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
