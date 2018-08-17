package io.pokerwars.bot.strategies;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static io.pokerwars.bot.model.in.Card.Rank.ACE;
import static io.pokerwars.bot.strategies.HandUtil.HIGH_ACE_COMPARATOR;
import static io.pokerwars.bot.strategies.HandUtil.getCardsInSequence;
import static io.pokerwars.bot.strategies.HandUtil.getDifferentRankCount;
import static io.pokerwars.bot.strategies.HandUtil.getHighestCard;
import static io.pokerwars.bot.strategies.HandUtil.getHighestGroupRank;
import static io.pokerwars.bot.strategies.HandUtil.getHighestPairRank;
import static io.pokerwars.bot.strategies.HandUtil.getMaxSameRankCount;
import static io.pokerwars.bot.strategies.HandUtil.getPairCount;
import static io.pokerwars.bot.strategies.HandUtil.getSameSuitCards;
import static io.pokerwars.bot.strategies.HandUtil.getSameSuitCount;
import static java.util.Arrays.asList;

import io.pokerwars.bot.model.in.Card;
import io.pokerwars.bot.model.in.Card.Rank;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum HandStrength {

  CARD_HIGH {
    @Override
    protected boolean match(final Set<Card> cards) {
      // always matches
      return true;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Card highest1 = getHighestCard(playerHand1.getCards());
      final Card highest2 = getHighestCard(playerHand2.getCards());

      final int value = highest1.getRank().compareTo(highest2.getRank());
      return value == 0 ? compareKicker(playerHand1, playerHand2) : value;
    }

    int compareKicker(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Card highCard = getHighestCard(playerHand1.getCards());

      return compareKicker(playerHand1.getCards(), playerHand2.getCards(),
          card -> card.getRank() != highCard.getRank(), 4);
    }

    protected int getHandValue() {
      return 1;
    }
  },
  PAIR {
    @Override
    protected boolean match(final Set<Card> cards) {
      return getMaxSameRankCount(cards) > 1;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank highestPairRank1 = getHighestPairRank(playerHand1.getCards());
      final Rank highestPairRank2 = getHighestPairRank(playerHand2.getCards());

      final int value = highestPairRank1.compareTo(highestPairRank2);
      return value == 0 ? compareKicker(playerHand1, playerHand2) : value;
    }

    protected int compareKicker(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank pairRank = getHighestPairRank(playerHand1.getCards());

      return compareKicker(playerHand1.getCards(), playerHand2.getCards(),
          card -> card.getRank() != pairRank, 3);
    }

    protected int getHandValue() {
      return 2;
    }
  },
  TWO_PAIRS {
    @Override
    protected boolean match(final Set<Card> cards) {
      return getPairCount(cards) > 1;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank highestPairRank1 = getHighestPairRank(playerHand1.getCards());
      final Rank highestPairRank2 = getHighestPairRank(playerHand2.getCards());

      int value = highestPairRank1.compareTo(highestPairRank2);
      if (value != 0) {
        return value;
      }

      value = compareSecondPair(playerHand1, playerHand2, highestPairRank1);
      return value != 0 ? value : compareKicker(playerHand1, playerHand2);
    }

    private int compareSecondPair(final PlayerHand playerHand1, final PlayerHand playerHand2,
        Rank firstPairRank) {
      final Rank highestPairRank1 = getHighestGroupRank(playerHand1.getCards(), 2, firstPairRank);
      final Rank highestPairRank2 = getHighestGroupRank(playerHand2.getCards(), 2, firstPairRank);
      return highestPairRank1.compareTo(highestPairRank2);
    }

    private int compareKicker(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank highestPairRank = getHighestPairRank(playerHand1.getCards());
      final Rank secondHighestPairRank =
          getHighestGroupRank(playerHand1.getCards(), 2, highestPairRank);
      return compareKicker(playerHand1.getCards(), playerHand2.getCards(),
          card -> card.getRank() != highestPairRank && card.getRank() != secondHighestPairRank, 1);
    }

    protected int getHandValue() {
      return 3;
    }
  },
  THREE_OF_A_KIND {
    @Override
    protected boolean match(final Set<Card> cards) {
      return getMaxSameRankCount(cards) > 2;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank highestTrisRank1 = getHighestGroupRank(playerHand1.getCards(), 3);
      final Rank highestTrisRank2 = getHighestGroupRank(playerHand2.getCards(), 3);

      final int value = highestTrisRank1.compareTo(highestTrisRank2);
      return value == 0 ? compareKicker(playerHand1, playerHand2) : value;
    }

    private int compareKicker(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Rank pairRank = getHighestGroupRank(playerHand1.getCards(), 3);
      return compareKicker(playerHand1.getCards(), playerHand2.getCards(),
          card -> card.getRank() != pairRank, 2);
    }

    protected int getHandValue() {
      return 4;
    }
  },
  STRAIGHT {
    @Override
    protected boolean match(final Set<Card> cards) {
      final Set<Card> sequence = getCardsInSequence(cards);
      return getDifferentRankCount(sequence) > 4;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Set<Card> cardsInSequence1 = getCardsInSequence(playerHand1.getCards());
      final Set<Card> cardsInSequence2 = getCardsInSequence(playerHand2.getCards());

      final Card highestCard1 = getHighestCard(cardsInSequence1);
      final Card highestCard2 = getHighestCard(cardsInSequence2);

      return highestCard1.getRank().compareTo(highestCard2.getRank());
    }

    protected int getHandValue() {
      return 5;
    }
  },
  FLUSH {
    @Override
    protected boolean match(final Set<Card> cards) {
      return getSameSuitCount(cards) > 4;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Set<Card> sameSuitCards1 = getSameSuitCards(playerHand1.getCards());
      final Set<Card> sameSuitCards2 = getSameSuitCards(playerHand2.getCards());

      return compareKicker(sameSuitCards1, sameSuitCards2, card -> true, 5);
    }

    protected int getHandValue() {
      return 6;
    }
  },
  FULL_HOUSE {
    @Override
    protected boolean match(final Set<Card> cards) {
      final long numberOfPairs = getPairCount(cards);
      final boolean hasTris = THREE_OF_A_KIND.match(cards);
      return hasTris && numberOfPairs == 2;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      Rank highestTrisRank1 = getHighestGroupRank(playerHand1.getCards(), 3);
      Rank highestTrisRank2 = getHighestGroupRank(playerHand2.getCards(), 3);

      final int value = highestTrisRank1.compareTo(highestTrisRank2);
      if (value == 0) { // we need to check who has the highest pair
        Rank highestPairRank1 = getHighestGroupRank(playerHand1.getCards(), 2, highestTrisRank1);
        Rank highestPairRank2 = getHighestGroupRank(playerHand2.getCards(), 2, highestTrisRank2);
        return highestPairRank1.compareTo(highestPairRank2);
      }
      return value;
    }

    protected int getHandValue() {
      return 7;
    }
  },
  POKER {
    @Override
    protected boolean match(final Set<Card> cards) {
      return getMaxSameRankCount(cards) > 3;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      Rank highestPokerRank1 = getHighestGroupRank(playerHand1.getCards(), 4);
      Rank highestPokerRank2 = getHighestGroupRank(playerHand2.getCards(), 4);

      final int value = highestPokerRank1.compareTo(highestPokerRank2);
      if (value == 0) { // we need to check who has the highest card
        Rank highestKickerRank1 = getHighestGroupRank(playerHand1.getCards(), 1, highestPokerRank1);
        Rank highestKickerRank2 = getHighestGroupRank(playerHand2.getCards(), 1, highestPokerRank2);
        return highestKickerRank1.compareTo(highestKickerRank2);
      }
      return value;
    }

    protected int getHandValue() {
      return 8;
    }
  },
  STRAIGHT_FLUSH {
    @Override
    protected boolean match(final Set<Card> cards) {
      final Set<Card> sameSuitCards = getSameSuitCards(cards);
      if (sameSuitCards.size() < 5) {
        return false;
      }
      return getCardsInSequence(sameSuitCards).size() > 4;
    }

    @Override
    protected int compare(final PlayerHand playerHand1, final PlayerHand playerHand2) {
      final Set<Card> cardsInSequence1 = getCardsInSequence(playerHand1.getCards());
      final Set<Card> cardsInSequence2 = getCardsInSequence(playerHand2.getCards());

      // since the ace can be considered either the max or min value in the sequence
      // it is sufficient to remove the ace and verify which player has the highest card
      final Card highestCard1 = getHighestCard(cardsInSequence1.stream()
          .filter(card -> card.getRank() != ACE).collect(Collectors.toSet()));
      final Card highestCard2 = getHighestCard(cardsInSequence2.stream()
          .filter(card -> card.getRank() != ACE).collect(Collectors.toSet()));

      return highestCard1.getRank().compareTo(highestCard2.getRank());
    }

    protected int getHandValue() {
      return 9;
    }
  };

  private static final Set<HandStrength> MATCHING_ORDER = newLinkedHashSet(asList(STRAIGHT_FLUSH,
      POKER, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIRS, PAIR, CARD_HIGH));

  protected static int compareKicker(final Set<Card> playerCards1, final Set<Card> playerCards2,
      final Predicate<Card> filter, final int limit) {
    final List<Card> kickers1 = playerCards1.stream().filter(filter)
        .sorted(HIGH_ACE_COMPARATOR.reversed()).limit(limit).collect(Collectors.toList());
    final List<Card> kickers2 = playerCards2.stream().filter(filter)
        .sorted(HIGH_ACE_COMPARATOR.reversed()).limit(limit).collect(Collectors.toList());

    int i = 0;
    int value;
    do {
      final Rank rank1 = kickers1.get(i).getRank();
      final Rank rank2 = kickers2.get(i).getRank();
      value = rank1.compareTo(rank2);
      i += 1;
    } while (value == 0 && i < kickers1.size());

    return value;
  }

  public static HandStrength computeHandStrength(final PlayerHand playerHand) {
    return MATCHING_ORDER.stream()
        .filter(matchingOrder -> matchingOrder.match(playerHand.getCards())).findFirst()
        .orElseThrow(RuntimeException::new);
  }

  protected abstract boolean match(final Set<Card> cards);

  protected abstract int compare(final PlayerHand playerHand1, final PlayerHand playerHand2);

  protected abstract int getHandValue();

}
