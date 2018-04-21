package io.pokerwars.bot.strategies;

import io.pokerwars.bot.model.in.GameInfo;
import io.pokerwars.bot.model.out.actions.Bet;
import io.pokerwars.bot.model.out.actions.Call;
import io.pokerwars.bot.model.out.actions.Check;
import io.pokerwars.bot.model.out.actions.Fold;
import io.pokerwars.bot.model.out.actions.PokerAction;
import io.pokerwars.bot.model.out.actions.Raise;
import io.pokerwars.util.RandomUtil;

public class RandomPokerStrategy {

  public static PokerAction play(GameInfo gameInfo) {
    boolean canCheckOrBet = gameInfo.canCheckOrBet();
    if (canCheckOrBet) {
      // if you can check or bet, don't fold as it's silly
      // since this is a random strategy, let's pick a random action between:
      // - check
      // - random bet
      int random = RandomUtil.randomInt(1, 2);
      switch (random) {
        case 1:
          // check
          return new Check();
        case 2:
          // random bet
          Long minBet = gameInfo.getMinBet();
          Long yourChips = gameInfo.getYourChips();
          Long randomBetChips = getRandomNumber(minBet, yourChips);
          return new Bet(randomBetChips);
        default:
          // default should never happen. Anyway check.
          return new Check();
      }
    } else { // gameInfo.canRaise() is true here
      // if you can't check, pick a random action between:
      // - fold
      // - call
      // - random raise
      int random = RandomUtil.randomInt(1, 3);
      switch (random) {
        case 1:
          // fold
          return new Fold();
        case 2:
          // call
          return new Call();
        case 3:
          // random raise
          Long minRaise = gameInfo.getMinRaise();
          Long yourChips = gameInfo.getYourChips();
          Long randomRaiseChips = getRandomNumber(minRaise, yourChips);
          return new Raise(randomRaiseChips);
        default:
          // default should never happen. Anyway fold.
          return new Fold();
      }
    }
  }

  private static Long getRandomNumber(Long minBetOrRaise, Long yourChips) {
    if (minBetOrRaise > yourChips) {
      // when your bot does not have enough chips to cover the minimum bet or raise, the only option
      // is to go all in
      return yourChips;
    } else {
      return RandomUtil.randomLong(minBetOrRaise, yourChips);
    }
  }

}
