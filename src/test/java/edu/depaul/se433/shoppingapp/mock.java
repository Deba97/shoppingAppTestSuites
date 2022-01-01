package edu.depaul.se433.shoppingapp;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class mock {

  private PurchaseDBO mockdb;
  private PurchaseAgent agent;
  @BeforeEach
  void setup() {

    // This loaded a default set of drivers.  See the PDF
    // for details
    mockdb = mock(PurchaseDBO.class);
    agent = new PurchaseAgent(mockdb);
  }

  @Test
  public void test(){
      //we want to verify that getPurchases calls dbo.getPurchases when called
    agent.getPurchases("x");
    verify(mockdb,times(1)).getPurchases(any());

  }

  @Test
  public void test2(){
    //we want to see if agent.getPurchases throws and error when dbo.getPurchases does
    when(mockdb.getPurchases(any())).thenThrow(new ArrayIndexOutOfBoundsException());
    assertDoesNotThrow(() -> agent.getPurchases(any()));
  }

  @Test
  public void test3(){
    //we want to see if agent.getPurchases reutnrs an empty list when errors occur
    when(mockdb.getPurchases(any())).thenThrow(new ArrayIndexOutOfBoundsException());
    assertEquals(new ArrayList<>(),agent.getPurchases(any()));
  }

  @Test
  public void test4(){
    //we want to see if average purchase works correctly
    Purchase one = new Purchase();
    one = one.make("Mike", LocalDate.of(2012,12,12),10.00,"IL","NEXT_DAY");
    Purchase two = new Purchase();
    two = two.make("Mike", LocalDate.of(2012,11,11),10.00,"IL","NEXT_DAY");
    Purchase three = new Purchase();
    three = three.make("Mike", LocalDate.of(2012,10,10),10.00,"IL","NEXT_DAY");

    List<Purchase> lst = new ArrayList<>();
    lst.add(one);
    lst.add(two);
    lst.add(three);

    when(mockdb.getPurchases(any())).thenReturn(lst);
    double answer = agent.averagePurchase(any());
    assertEquals(10,answer);

    }

  @Test
  public void test5(){
    //we want to see if passing in an empty list makes averagePurchase return 0

    when(mockdb.getPurchases(any())).thenReturn(new ArrayList<>());
    double answer = agent.averagePurchase(any());
    assertEquals(0,answer);

  }

  @Test
  public void test6(){
    //we want to see if the save function works
    Purchase one = new Purchase();
    one = one.make("Mike", LocalDate.of(2012,12,12),10.00,"IL","NEXT_DAY");
    agent.save(one);
    verify(mockdb,times(1)).savePurchase(any());
  }

}
