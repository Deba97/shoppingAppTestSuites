/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import org.jdbi.v3.core.CloseException;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * The Purchase agent uses aPurchaseDBO to save and retrieve purchase information.
 * The business logic should be in this class while the details of database connections
 * etc. should be in PurchaseDBO
 */
public class PurchaseAgent {
  private PurchaseDBO dbo;

  private static final Logger log = LoggerFactory.getLogger(PurchaseAgent.class);

  public PurchaseAgent(PurchaseDBO dbo) {
    this.dbo = dbo;
  }

  public void save(Purchase purchase) {
    if (! purchase.equals(null)) {
      dbo.savePurchase(purchase);
    }
  }

  public List<Purchase> getPurchases(String name) {
    // TODO implement this function using the dbo object
    List<Purchase> result;
    ArrayList<Purchase> empty = new ArrayList<>();
    try{
        result = dbo.getPurchases(name);
    }
    catch(Exception e){
        log.debug("{} has no purchases ",name);
        return empty;
    }

    return result;
  }

  public double averagePurchase(String user) {
    List<Purchase> purchases = dbo.getPurchases(user);
    short cnt = 0;
    double total = 0.0;
    for (short i = 0; i < purchases.size(); i++) {
      Purchase p = purchases.get(i);
      cnt++;
      total += p.getCost();
    }
    if (cnt > 0) {
      return total / cnt;
    } else {
      return 0.0;
    }
  }
}
