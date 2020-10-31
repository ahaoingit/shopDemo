package com.brianyi.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author ahao 2020-10-21
 */
public class ShoppingID {
   private static AtomicInteger integer = new AtomicInteger(1);
   private ShoppingID(){}
   public static Integer getShoppingId() {
       return integer.getAndAdd(1);
   }

}
