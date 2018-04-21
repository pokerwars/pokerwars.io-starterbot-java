package io.pokerwars.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

  public static int randomInt(int minIncluded, int maxIncluded) {
    return ThreadLocalRandom.current().nextInt(minIncluded, maxIncluded + 1);
  }

  public static Long randomLong(long minIncluded, long maxIncluded) {
    return ThreadLocalRandom.current().nextLong(minIncluded, maxIncluded + 1);
  }

}
