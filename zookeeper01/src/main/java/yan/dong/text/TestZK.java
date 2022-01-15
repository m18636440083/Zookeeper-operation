package yan.dong.text;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestZK {

    // Zookeeper集群IP
    private String connectString = "192.168.184.128:2181,192.168.184.129:2181,192.168.184.130:2181";
    private ZooKeeper zookeeper;

    /*
        session超时 60秒：一定不能太少，因为连接zookeeper和加载集群环境会因为性能原因延迟略高 如果时间太少，
        还没有创建好客户端，就开始操作节点，会报错的
     */
    private int sessionTimeout = 60 * 1000;

    @Before
    public void init() throws IOException {
        zookeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("业务处理代码");
                System.out.println(watchedEvent.getType());
            }
        });
    }

    // 创建节点
    @Test
    public void createNode() throws InterruptedException, KeeperException {
        // 参数1：要创建的节点的路径
        // 参数2：节点数据
        // 参数3：节点权限
        // 参数4：节点的类型
        String str = zookeeper.create("/France", "bali".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("该节点已经创建");
    }

    // 获取节点内容
    @Test
    public void getNodeData() throws InterruptedException, KeeperException {
        byte[] data = zookeeper.getData("/japan", false, new Stat());
        String information = new String(data);
        System.out.println("查询到的数据为" + information);

    }

    // 修改节点内容
    @Test
    public void updateData() throws InterruptedException, KeeperException {
        Stat stat = zookeeper.setData("/France", "lufugong".getBytes(), 0);
        System.out.println(stat);
    }

    // 删除节点
    @Test
    public void delete() throws InterruptedException, KeeperException {
        zookeeper.delete("/France", 1);
        System.out.println("删除成功");
    }

    // 获取子节点
    @Test
    public void getChildren() throws InterruptedException, KeeperException {
        List<String> list = zookeeper.getChildren("/meituan", false);
        for (String children : list) {
            System.out.println(children);
        }
    }

    // 监听子节点的变化
    @Test
    public void watchNode() throws InterruptedException, KeeperException, IOException {
        List<String> list = zookeeper.getChildren("/", true);
        for (String children : list) {
            System.out.println(children);
        }

        // 让线程不停止，等待监听响应
        System.in.read();
    }

    // 判断Node是否存在
    @Test
    public void exist() throws InterruptedException, KeeperException {
        Stat stat = zookeeper.exists("/dog", false);
        System.out.println(stat == null ? "不存在" : "存在");
    }
}
