package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

@SpringBootApplication
@RestController
public class DemoApplication {
  @Autowired
	DataSource dataSource;

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  /**
   * curl http://localhost:8080/
   * @return Map
   */
  @GetMapping("/")
  public String hello() {
    try {
      // 连接数据库Connection（注意要关闭）
      Connection connection = dataSource.getConnection();
      // 创建操作指令Statement（注意要关闭）
      Statement statement = connection.createStatement();
      // executeQuery执行查询指令，获取查询结果 ResultSet
      ResultSet resultSet = statement.executeQuery("select * from user");

      // 遍历result获取结果集
      Map<String, String> map = new HashMap<>();
      while (resultSet.next()) {
        String id = resultSet.getString("user_id");
        String name = resultSet.getString("user_name");
        map.put(id, name);
      }
      // 关闭链接
      statement.close();
      connection.close();
      // 先转成json字符串再返回
      return JSON.toJSONString(map);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "数据获取错误";
  }
}