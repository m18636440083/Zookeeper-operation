package yan.dong.meituan;

import org.apache.zookeeper.*;

import java.io.IOException;
/*
    商家类
 */
public class ShopServer {

    private String connectString = "192.168.184.128:2181,192.168.184.129:2181,192.168.184.130:2181";

    private int sessionTimeout = 60 * 1000;

    private ZooKeeper zooKeeper;

    // 创建Zookeeper的客户端连接
    public void connect() throws Exception {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }
    // 注册到Zookeeper集群
    public void register(String shopName) throws InterruptedException, KeeperException {
        // 一定要创建CreateMode.EPHEMERAL_SEQUENTIAL，临时有序节点来进行营业
        // 一来可以自动编号，二来可以断开连接时自动删除节点（打烊）
        String s = zooKeeper.create("/meituan/shop", shopName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(shopName + "开始营业了" + s);
    }

    public void business(String shopName) throws IOException {
        System.out.println(shopName + "正在营业中");
        System.in.read();
    }

    public static void main(String[] args) throws Exception {
        // 1.开自己的商铺
        ShopServer shopServer = new ShopServer();
        // 2.连接Zookeeper集群（和美团取得联系）
        shopServer.connect();
        // 3.将服务器节点注册（入驻美团）
        shopServer.register(args[0]);
        // 4.业务逻辑处理（做生意）
        shopServer.business(args[0]);
    }
}
