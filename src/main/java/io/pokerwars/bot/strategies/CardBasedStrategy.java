package io.pokerwars.bot.strategies;

import static io.pokerwars.bot.strategies.HandStrength.computeHandStrength;

import io.pokerwars.bot.model.in.Card;
import io.pokerwars.bot.model.in.GameInfo;
import io.pokerwars.bot.model.out.actions.Bet;
import io.pokerwars.bot.model.out.actions.Call;
import io.pokerwars.bot.model.out.actions.Check;
import io.pokerwars.bot.model.out.actions.Fold;
import io.pokerwars.bot.model.out.actions.PokerAction;
import io.pokerwars.bot.model.out.actions.Raise;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardBasedStrategy {

  private static final Logger LOG = LoggerFactory.getLogger(CardBasedStrategy.class);

  static PokerAction play(GameInfo gameInfo, StrategyConfig strategyConfig) {
    int cardsValue = doYouHaveGoodCards(gameInfo.getYourCards(), gameInfo.getTableCards());
    Long minBet = gameInfo.getMinBet();
    Long minRaise = gameInfo.getMinRaise();
    Long yourChips = gameInfo.getYourChips();
    LOG.info("minRaise: " + minRaise + " yourChips: " + yourChips + " chips to call: " + gameInfo.getChipsToCall());

    if (gameInfo.canCheckOrBet()) {
      if (cardsValue == 1 && !strategyConfig.isAggressivePlayer()) {
        return new Check();
      } else {
        return new Bet(Math.min(minBet * cardsValue, yourChips));
      }
    } else { // gameInfo.canRaise() is true here
      Long chipsToCall = gameInfo.getChipsToCall();
      Long yourChipsForRaise = gameInfo.getYourChips() - chipsToCall;

      //with CARD_HIGH or PAIR
      if (cardsValue == 1 || cardsValue == 2) {
        PokerAction bluff = bluffingStrategy(gameInfo, strategyConfig);
        if (bluff != null) {
          return bluff;
        }
        if (!strategyConfig.isAggressivePlayer()) {
          return new Fold();
        } else {
          return new Call();
        }
      }

      //with TWO_PAIRS
      if (cardsValue == 3) {
        PokerAction bluff = bluffingStrategy(gameInfo, strategyConfig);
        if (bluff != null) {
          return bluff;
        }
        if (strategyConfig.getAggressiveness() == 0) {
          return new Fold();
        } else if (strategyConfig.getRaiseFactor() == 0) {
          return new Call();
        } else {
          return new Raise(Math.min(minRaise * strategyConfig.getRaiseFactor(), yourChipsForRaise));
        }
      }

      //with THREE_OF_A_KIND
      if (cardsValue == 4) {
        PokerAction bluff = bluffingStrategy(gameInfo, strategyConfig);
        if (bluff != null) {
          return bluff;
        }
        if (strategyConfig.getRaiseFactor() == 0) {
          return new Call();
        } else {
          return new Raise(
              Math.min(minRaise * (strategyConfig.getRaiseFactor() + 1), yourChipsForRaise));
        }
      }

      //with STRAIGHT or FLUSH
      if (cardsValue == 5 || cardsValue == 6) {
        return new Raise(
            Math.min(minRaise * (strategyConfig.getRaiseFactor() + 2), yourChipsForRaise));
      }

      //with FULL_HOUSE, POKER or STRAIGHT_FLUSH
      if (cardsValue >= 7) {
        if (strategyConfig.getRaiseFactor() > 3) {
          return new Raise(yourChips);
        } else {
          return new Raise(
              Math.min(minRaise * (strategyConfig.getRaiseFactor() + 3), yourChipsForRaise));
        }
      }
    }

    //default case
    return new Fold();
  }

  private static PokerAction bluffingStrategy(GameInfo gameInfo, StrategyConfig strategyConfig) {
    // 5 means 50% bluff
    Integer bluffProbability = strategyConfig.getBluff() * 10;

    int random = new Random().nextInt(100);

    if (random <= bluffProbability) {
      int bluff = new Random().nextInt(20);
      Long minRaise = gameInfo.getMinRaise();
      Long chipsToCall = gameInfo.getChipsToCall();
      Long yourChipsForRaise = gameInfo.getYourChips() - chipsToCall;
      if (bluff > 15) {
        LOG.info("super bluff");
        return new Raise(yourChipsForRaise);
      }
      if (bluff > 10) {
        LOG.info("bluff");
        return new Raise(Math.min(minRaise * 10, yourChipsForRaise));
      }
    }
    return null;
  }

  private static int doYouHaveGoodCards(Set<Card> yourCards, Set<Card> tableCards) {
    HandStrength handStrength = computeHandStrength(new PlayerHand(yourCards, tableCards));
    LOG.info("My BOT cards are: " + handStrength.name());

    return handStrength.getHandValue();
  }

}