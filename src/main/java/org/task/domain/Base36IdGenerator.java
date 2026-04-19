package org.task.domain;

import java.util.Random;

/** Generates random Base36 identifiers using alphanumeric characters. */
public class Base36IdGenerator implements IdGenerator {
  private static String KeySpace = "abcdefghijklmnopqrstuvwxyz0123456789";
  private static int SIZE = 7;
  private static Random random = new Random();

  @Override
  public String getKey() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < SIZE; i++) {
      sb.append(KeySpace.charAt(random.nextInt(KeySpace.length())));
    }
    return sb.toString();
  }
}
