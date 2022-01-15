package yan.dong.meituan;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    用户类
 */
public class Customers {

    private String connectString = "192.168.184.128:2181,192.168.184.129:2181,192.168.184.130:2181";

    private int sessionTimeout = 60 * 1000;

    private ZooKeeper zooKeeper = null;

    public void connect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    getChildren();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getChildren() throws Exception {
        // 1.获取服务器子节点信息，并对父节点进行监听
        List<String> shops = zooKeeper.getChildren("/meituan", true);
        // 2. 存储服务器信息列表
        ArrayList<String> shoplists = new ArrayList<>();
        // 3. 遍历所有节点，获取节点中的主机名称信息
        for (String shop : shops) {
            byte[] data = zooKeeper.getData("/meituan/" + shop, false, new Stat());
            String information = new String(data);
            shoplists.add(information);
        }
        // 4. 打印服务器列表信息
        System.out.println("目前正在营业的商家" + shoplists);
    }

    public void business() throws IOException {
        System.out.println("用户正在浏览");
        System.in.read();
    }
    public static void main(String[] args) throws Exception {
        // 1. 获取连接（用户打开美团）
        Customers customers = new Customers();
        customers.connect();
        // 2. 获取子节点信息，从中获取服务器信息列表（从美团中获取商家信息列表）
        customers.getChildren();
        // 3. 业务进程（用户点餐）
        customers.business();
    }

}
