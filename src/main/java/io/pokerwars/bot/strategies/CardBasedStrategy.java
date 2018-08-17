package io.pokerwars.bot.strategies;

import static io.pokerwars.bot.strategies.HandStrength.computeHandStrength;

import io.pokerwars.bot.model.in.GameInfo;
import io.pokerwars.bot.model.out.actions.Bet;
import io.pokerwars.bot.model.out.actions.Call;
import io.pokerwars.bot.model.out.actions.Check;
import io.pokerwars.bot.model.out.actions.Fold;
import io.pokerwars.bot.model.out.actions.PokerAction;
import io.pokerwars.bot.model.out.actions.Raise;
import java.util.Optional;
import java.util.Random;

public class CardBasedStrategy {

  static PokerAction play(GameInfo gameInfo, StrategyConfig strategyConfig) {
    int cardsStrength = computeHandStrength(
        new PlayerHand(gameInfo.getYourCards(), gameInfo.getTableCards())).getHandValue();

    Long minBet = gameInfo.getMinBet();
    Long minRaise = gameInfo.getMinRaise();
    Long yourChips = gameInfo.getYourChips();

    if (gameInfo.canCheckOrBet()) {
      return canCheckOrBetStrategy(strategyConfig, cardsStrength, minBet, yourChips);
    } else { // gameInfo.canRaise() is true here
      Long chipsToCall = gameInfo.getChipsToCall();
      Long yourChipsForRaise = yourChips - chipsToCall;

      //with CARD_HIGH or PAIR
      if (cardsStrength == 1 || cardsStrength == 2) {
        return bluffingStrategy(gameInfo, strategyConfig)
            .orElse(highCardOrPairStrategy(strategyConfig));
      }

      //with TWO_PAIRS
      if (cardsStrength == 3) {
        return bluffingStrategy(gameInfo, strategyConfig)
            .orElse(twoPairsStrategy(strategyConfig, minRaise, yourChipsForRaise));
      }

      //with THREE_OF_A_KIND
      if (cardsStrength == 4) {
        return bluffingStrategy(gameInfo, strategyConfig)
            .orElse(threeOfAKindStrategy(strategyConfig, minRaise, yourChipsForRaise));
      }

      //with STRAIGHT or FLUSH
      if (cardsStrength == 5 || cardsStrength == 6) {
        return StraightOrFlushStrategy(strategyConfig, minRaise, yourChipsForRaise);
      }

      //with FULL_HOUSE, POKER or STRAIGHT_FLUSH
      if (cardsStrength >= 7) {
        return bestCardsStrategy(strategyConfig, minRaise, yourChipsForRaise);
      }
    }

    //default case
    return new Fold();
  }

  private static PokerAction bestCardsStrategy(StrategyConfig strategyConfig, Long minRaise,
      Long yourChipsForRaise) {
    if (strategyConfig.getRaiseFactor() > 3) {
      return new Raise(yourChipsForRaise);
    } else {
      return new Raise(
          Math.min(minRaise * (strategyConfig.getRaiseFactor() + 3), yourChipsForRaise));
    }
  }

  private static Raise StraightOrFlushStrategy(StrategyConfig strategyConfig, Long minRaise,
      Long yourChipsForRaise) {
    return new Raise(
        Math.min(minRaise * (strategyConfig.getRaiseFactor() + 2), yourChipsForRaise));
  }

  private static PokerAction threeOfAKindStrategy(StrategyConfig strategyConfig, Long minRaise,
      Long yourChipsForRaise) {
    if (strategyConfig.getRaiseFactor() == 1) {
      return new Call();
    } else {
      return new Raise(
          Math.min(minRaise * (strategyConfig.getRaiseFactor() + 1), yourChipsForRaise));
    }
  }

  private static PokerAction twoPairsStrategy(StrategyConfig strategyConfig, Long minRaise,
      Long yourChipsForRaise) {
    if (strategyConfig.getAggressiveness() == 1) {
      return new Fold();
    } else if (strategyConfig.getRaiseFactor() <= 2) {
      return new Call();
    } else {
      return new Raise(Math.min(minRaise * strategyConfig.getRaiseFactor(), yourChipsForRaise));
    }
  }

  private static PokerAction highCardOrPairStrategy(StrategyConfig strategyConfig) {
    return strategyConfig.isAggressivePlayer() ? new Call() : new Fold();
  }

  private static PokerAction canCheckOrBetStrategy(StrategyConfig strategyConfig, int cardsStrength,
      Long minBet, Long yourChips) {
    if (cardsStrength == 1 && strategyConfig.isNotAggressivePlayer()) {
      return new Check();
    } else {
      return new Bet(Math.min(minBet * cardsStrength * strategyConfig.getRaiseFactor(), yourChips));
    }
  }

  private static Optional<PokerAction> bluffingStrategy(GameInfo gameInfo,
      StrategyConfig strategyConfig) {
    // 5 means 50% bluff
    Integer bluffConfig = strategyConfig.getBluff() * 10;

    int bluffPercentage = new Random().nextInt(100);

    if (bluffPercentage <= bluffConfig) {
      int bluffAmount = new Random().nextInt(20);
      Long minRaise = gameInfo.getMinRaise();
      Long chipsToCall = gameInfo.getChipsToCall();
      Long yourChipsForRaise = gameInfo.getYourChips() - chipsToCall;
      if (bluffAmount > 15) {
        return Optional.of(new Raise(yourChipsForRaise));
      }
      if (bluffAmount > 10) {
        return Optional.of(new Raise(Math.min(minRaise * 10, yourChipsForRaise)));
      }
    }
    return Optional.empty();
  }

}