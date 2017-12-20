///*     */ package com.hl.test;
///*     */ 
///*     */ import java.io.IOException;
///*     */ import java.io.PrintStream;
///*     */ import java.util.Properties;
///*     */ import org.apache.hadoop.conf.Configuration;
///*     */ import org.apache.hadoop.hbase.HBaseConfiguration;
///*     */ import org.apache.hadoop.hbase.HColumnDescriptor;
///*     */ import org.apache.hadoop.hbase.HTableDescriptor;
///*     */ import org.apache.hadoop.hbase.TableName;
///*     */ import org.apache.hadoop.hbase.client.Admin;
///*     */ import org.apache.hadoop.hbase.client.Connection;
///*     */ import org.apache.hadoop.hbase.client.ConnectionFactory;
///*     */ import org.apache.hadoop.hbase.client.Get;
///*     */ import org.apache.hadoop.hbase.client.Put;
///*     */ import org.apache.hadoop.hbase.client.Result;
///*     */ import org.apache.hadoop.hbase.client.ResultScanner;
///*     */ import org.apache.hadoop.hbase.client.Scan;
///*     */ import org.apache.hadoop.hbase.client.Table;
///*     */ import org.apache.log4j.Logger;
///*     */ 
///*     */ public class HbaseTest
///*     */ {
///*     */   public static Configuration conf;
///*     */   public static Connection conn;
///*     */   public static final String FAMILY = "msg";
///*     */   public static final String ROOMID = "room";
///*  27 */   public static String _prefix = "room";
///*  28 */   public static Logger log = Logger.getLogger(HbaseTest.class);
///*     */ 
///*  45 */   private static int count = 0;
///*     */ 
///*     */   static
///*     */   {
///*     */     try
///*     */     {
///*  31 */       Properties pro = new Properties();
///*  32 */       pro.load(HbaseTest.class.getResourceAsStream("conf.properties"));
///*  33 */       _prefix = pro.getProperty("prefix");
///*  34 */       conf = HBaseConfiguration.create();
///*  35 */       conf.set("hbase.zookeeper.property.clientPort", "2181");
///*  36 */       conf.set("hbase.zookeeper.quorum", pro.getProperty("ip"));
///*     */ 
///*  38 */       conn = ConnectionFactory.createConnection(conf);
///*     */     }
///*     */     catch (IOException e) {
///*  41 */       e.printStackTrace();
///*     */     }
///*     */   }
///*     */ 
///*     */   public static synchronized int getCount()
///*     */   {
///*  50 */     return count;
///*     */   }
///*     */ 
///*     */   public static synchronized void addtCount() {
///*  54 */     count += 1;
///*     */   }
///*     */ 
///*     */   public static void main(String[] args)
///*     */     throws InterruptedException
///*     */   {
///*  88 */     deleteTable();
///*     */   }
///*     */ 
///*     */   public static void deleteTable()
///*     */   {
///*     */     try {
///*  94 */       Admin admin = conn.getAdmin();
///*     */ 
///* 102 */       System.out.println("delete");
///* 103 */       TableName[] tablesNames = admin.listTableNames();
///* 104 */       int count = tablesNames.length;
///* 105 */       System.out.println("COUNT:" + tablesNames.length);
///* 106 */       for (TableName table : tablesNames) {
///* 107 */         if ((admin.tableExists(table)) && 
///* 108 */           (table.getNameAsString().startsWith(_prefix))) {
///* 109 */           admin.disableTable(table);
///* 110 */           admin.deleteTable(table);
///* 111 */           System.out.println("DELETE:" + table.getNameAsString());
///*     */         }
///*     */ 
///* 114 */         System.out.println("还剩:" + --count);
///*     */       }
///*     */     }
///*     */     catch (Exception e) {
///* 118 */       e.printStackTrace();
///*     */     }
///*     */   }
///*     */ 
///*     */   public static void createTable(String tableName)
///*     */   {
///*     */     try
///*     */     {
///* 126 */       Admin admin = conn.getAdmin();
///* 127 */       TableName table = TableName.valueOf(tableName);
///* 128 */       if (!admin.tableExists(table))
///*     */       {
///* 133 */         HTableDescriptor tableDescriptor = new HTableDescriptor(table);
///* 134 */         HColumnDescriptor columnDescriptor = new HColumnDescriptor("msg");
///*     */ 
///* 137 */         tableDescriptor.addFamily(columnDescriptor);
///* 138 */         admin.createTable(tableDescriptor);
///*     */       }
///*     */     }
///*     */     catch (Exception e) {
///* 142 */       e.printStackTrace();
///*     */       try {
///* 144 */         if (conn != null)
///* 145 */           conn.close();
///*     */       }
///*     */       catch (IOException ie)
///*     */       {
///* 149 */         ie.printStackTrace();
///*     */       }
///*     */     }
///*     */   }
///*     */ 
///*     */   public static void insert(String tableName, String rowId, String userId, String content)
///*     */   {
///*     */     try
///*     */     {
///* 158 */       Put put = new Put(rowId.getBytes());
///* 159 */       put.addColumn("msg".getBytes(), "content".getBytes(), content.getBytes());
///* 160 */       put.addColumn("msg".getBytes(), "userId".getBytes(), userId.getBytes());
///* 161 */       conn.getTable(TableName.valueOf(tableName)).put(put);
///*     */     }
///*     */     catch (IOException e) {
///* 164 */       e.printStackTrace();
///*     */       try {
///* 166 */         if (conn != null)
///* 167 */           conn.close();
///*     */       }
///*     */       catch (IOException ie)
///*     */       {
///* 171 */         ie.printStackTrace();
///*     */       }
///*     */     }
///*     */   }
///*     */ 
///*     */   public static void find(String tableName, String rowId)
///*     */   {
///*     */     try {
///* 179 */       TableName table = TableName.valueOf(tableName);
///* 180 */       Table t = conn.getTable(table);
///* 181 */       Result rs = t.get(new Get(rowId.getBytes()));
///* 182 */       String content = new String(rs.getValue("msg".getBytes(), "content".getBytes()));
///* 183 */       String userId = new String(rs.getValue("msg".getBytes(), "userId".getBytes()));
///* 184 */       System.out.println("user:" + userId + "    speak:" + content);
///*     */     }
///*     */     catch (Exception e) {
///* 187 */       e.printStackTrace();
///*     */       try {
///* 189 */         if (conn != null)
///* 190 */           conn.close();
///*     */       }
///*     */       catch (IOException ie)
///*     */       {
///* 194 */         ie.printStackTrace();
///*     */       }
///*     */     }
///*     */   }
///*     */ 
///*     */   public static void query(String tableName) {
///*     */     try {
///* 201 */       Scan scan = new Scan();
///* 202 */       scan.addColumn("msg".getBytes(), "userId".getBytes());
///* 203 */       scan.addColumn("msg".getBytes(), "content".getBytes());
///*     */ 
///* 206 */       scan.setStartRow("1".getBytes());
///* 207 */       scan.setStopRow("2".getBytes());
///* 208 */       TableName table = TableName.valueOf(tableName);
///* 209 */       Table t = conn.getTable(table);
///* 210 */       ResultScanner rs = t.getScanner(scan);
///* 211 */       for (Result r : rs)
///*     */       {
///* 213 */         String content = new String(r.getValue("msg".getBytes(), "content".getBytes()));
///* 214 */         String userId = new String(r.getValue("msg".getBytes(), "userId".getBytes()));
///* 215 */         System.out.println("user:" + userId + "   speak:" + content);
///*     */       }
///*     */     }
///*     */     catch (Exception e) {
///* 219 */       e.printStackTrace();
///*     */       try {
///* 221 */         if (conn != null)
///* 222 */           conn.close();
///*     */       }
///*     */       catch (IOException ie)
///*     */       {
///* 226 */         ie.printStackTrace();
///*     */       }
///*     */     }
///*     */   }
///*     */ }
//
///* Location:           D:\java\delHbaseByPrefix\
// * Qualified Name:     com.hl.test.HbaseTest
// * JD-Core Version:    0.6.2
// */