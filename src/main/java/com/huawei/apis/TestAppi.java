package com.huawei.apis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import sun.net.spi.nameservice.NameServiceDescriptor;

import javax.xml.stream.XMLOutputFactory;
import java.io.IOException;
import java.util.Iterator;

public class TestAppi {
    private static Connection connection;
    private static Admin admin;
    public static void main(String[] args) {

    }
    /*
    * DDL:
    * 判断表是否在存在
    * 创建表
    * 创建命名空间
    * 删除表
    *
    * DML：
    * 插入数据
    * 查数据get
    * 查数据scan
    * 删除数据
    *
    * */
    //资源关闭方法
    public void close() {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //判断表是否在存在
    public static boolean isTableExists(String tableName) throws IOException {
        //获取配置对象
        Configuration entries1 = HBaseConfiguration.create();
        //HBaseConfiguration entries = new HBaseConfiguration();
        //如果zookeeper使用的是默认端口2181，则下面hadoop001:2181不需要写2181
        //连接zookeeper
        entries1.set("hbase.zookeeper.quorum", "hadoop01,hadoop02,hadoop03");
        //HBaseAdmin hBaseAdmin = null;
        //获取hbase client对象
        connection = ConnectionFactory.createConnection(entries1);
        Admin admin = connection.getAdmin();
        Boolean isExists = false;
        try {
            //valueOf()
            isExists = admin.tableExists(TableName.valueOf("yuanlibo"));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            admin.close();
        }
        return isExists;
    }

    //创建表
    public static void createTable(String tableName, String... cfs) throws IOException {
        //判断传入的列簇是否正确
        if (cfs.length < 0) {
            System.out.println("please input correct cfs");
            return;
        }
        //判断表是否存在,如果存在则报异常，不存在则开始创建表
        if (isTableExists(tableName)) {
            System.out.println("this" + tableName +"existed");
        } else {
            //创建表描述器
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            //循环添加列簇信息
            for (String cf : cfs) {
                //创建列簇描述器
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            //创建表
            admin.createTable(hTableDescriptor);
        }
    }

    //删除表
    public static void dropTable(String tableName) throws IOException {
        //判断表是否存在
        if (!isTableExists(tableName)) {
            System.out.println(" this" + tableName + "is not exist");
            return;
        }
        //先disable 表
        admin.disableTable(TableName.valueOf(tableName));
        //在删除表
        admin.deleteTable(TableName.valueOf(tableName));
    }

    //创建命名空间
    public static void createNamespace(String nameSpace) {
        //创建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(nameSpace).build();
        //创建命名空间
        try {
            admin.createNamespace(namespaceDescriptor);
        } catch (NamespaceExistException e) {//官方没有提供判断命名空间是否存在的方法只能手动捕获异常类
            System.out.println(nameSpace + "namespce has been existed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //表插入数据
    public static void put(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException {
        //获取表对象
        Table table = connection.getTable(TableName.valueOf(tableName));
        //获取Putd对象
        Put put = new Put(Bytes.toBytes(rowKey));
        //Put设置列簇，列，值
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        //插入数据
        table.put(put);
        //关闭table
        table.close();
    }

    //获取数据
    public static void get(String tableName, String rowKey, String columnFamily, String column) throws IOException {
        Table table = connection.getTable(TableName.valueOf("yuanlibo"));
        Get get = new Get(Bytes.toBytes(rowKey));
        //获取指定的列簇的数据
        get.addFamily(Bytes.toBytes(columnFamily));
        //获取指定列的数据
        get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        //设置获取数据的版本数
        Get get1 = get.setMaxVersions();
        Result result = table.get(get1);
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            System.out.println(Bytes.toString(CellUtil.cloneFamily(cell))
                                + Bytes.toString(CellUtil.cloneQualifier(cell))
                                + Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }
    public static void scan(String tableName) throws IOException {
        Table table = connection.getTable(TableName.valueOf("yuanlibo"));
        Scan scan = new Scan();//空参构造代表扫描全表
        //Scan scan1 = new Scan(Bytes.toBytes("1001"), Bytes.toBytes("1003"));
        //行键过滤器
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER, new BinaryComparator(Bytes.toBytes("yuanlibo")));
        //列簇过滤器
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("colunm+family")));
        Scan scan1 = new Scan(Bytes.toBytes("1001"), rowFilter);
        //如果给scanner设置多个过滤器则需要使用FilterList,
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, rowFilter, familyFilter);
        //给scan设置多个过滤器
        Scan scan2 = scan.setFilter(filterList);
        ResultScanner scanner = table.getScanner(scan1);
        for (Result next : scanner) {
            Cell[] cells = next.rawCells();
            for (Cell cell : cells) {
                System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        table.close();

    }
    public static void delete(String tableName, String rowKey, String FamiluColumn,String Column) throws IOException {
        Table table = connection.getTable(TableName.valueOf("tableName"));
        Delete delete = new Delete(Bytes.toBytes("1005"));
        /*//设置删除的列
        delete.addColumn();
        //设置删除的列，删除所有版本
        delete.addColumns();
        //加时间戳，删除指定时间戳版本的数据
        delete.addColumn();
        //加时间戳，删除小于等于时间戳的数据
        delete.addColumns()
*/

        table.delete(delete);//只指定rowkey相当于deleteAll
        table.close();

    }

}
