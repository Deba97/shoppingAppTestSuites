/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Script;
/**
 * Manages the database connection related to purchases
 */
public class PurchaseDBO {

  private static String jdbcUrl;
  private Jdbi jdbi;

  public PurchaseDBO() throws IOException {
    initialize();
  }

  private void savePurchase(String name, LocalDate purchaseDate, double cost, String state, String shipping) {

    jdbi.useHandle(handle -> {
      handle.createUpdate("INSERT INTO purchases(name, pdate, cost, state, shipping_type) VALUES (?, ?, ?, ?, ?)")
          .bind(0, name)
          .bind(1, purchaseDate)
          .bind(2, cost)
          .bind(3, state)
          .bind(4, shipping)
          .execute();
          });
  }

  public void savePurchase(Purchase p) {
    savePurchase(
            p.getCustomerName(),
            p.getPurchaseDate(),
            p.getCost(),
            p.getState(),
            p.getShipping()
            );
  }
  
  public List<Purchase> getPurchases(String user) {
    String sql = "SELECT * FROM purchases WHERE name = '" + user + "' ORDER BY id_num";
    List<Purchase> purchases = jdbi.withHandle(handle -> {
      return handle.createQuery(sql)
          .mapToBean(Purchase.class)
          .list();
    });

    return purchases;
  }

  private void initialize() throws IOException {
    jdbcUrl  = "jdbc:h2:" + System.getProperty("user.dir") + "/src/main/resources/purchases";
    DataSource ds = JdbcConnectionPool.create(jdbcUrl,
        "sa",
        "");
    jdbi = Jdbi.create(ds);

    String script = getScript("create.sql");
    int[] results = jdbi.withHandle(handle -> {
      Script s = new Script(handle, script);
      return s.execute();
    });

    initializeUsers();
  }

  private void initializeUsers() {
    List<User> users = getUsers();
    if (users.size() == 0) {
      addUser("Sam", "Pass1234");
      addUser("Carol", "Temp$");
      addUser("Ahmed", "Cat.feet#");
      addUser("Latisha", "AWXSwesd");
    }
  }

  public List<User> getUsers() {
    String sql = "SELECT * FROM users";
    List<User> users = jdbi.withHandle(handle -> handle.createQuery(sql)
        .mapToBean(User.class)
        .list());
    return users;
  }

  public void addUser(String name, String password) {
    jdbi.useHandle(handle -> {
      handle.createUpdate("INSERT INTO users(name, password) VALUES (?, ?)")
          .bind(0, name)
          .bind(1, password)
          .execute();
    });
  }

  private String getScript(String scriptName) {
    InputStream in = getClass().getResourceAsStream("/" + scriptName);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    return reader.lines().collect(Collectors.joining("\n"));
  }
}
