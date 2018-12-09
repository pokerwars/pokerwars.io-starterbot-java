package io.pokerwars.bot.strategies;

import static io.pokerwars.bot.model.in.Card.Rank.ACE;
import static io.pokerwars.bot.model.in.Card.Rank.DEUCE;
import static java.lang.String.format;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import io.pokerwars.bot.model.in.Card;
import io.pokerwars.bot.model.in.Card.Rank;
import io.pokerwars.bot.model.in.Card.Suit;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collector;

public final class HandUtil {

  public static final Comparator<Card> HIGH_ACE_COMPARATOR = new AceComparator(false);
  private static final Comparator<Card> LOW_ACE_COMPARATOR = new AceComparator();

  private HandUtil() {
  }

  public static Card getHighestCard(final Set<Card> cards) {
    return cards.stream().sorted().findFirst().orElseThrow(RuntimeException::new);
  }

  public static Rank getHighestPairRank(final Set<Card> cards) {
    return getHighestGroupRank(cards, 2);
  }

  static Rank getHighestGroupRank(final Set<Card> cards, final int sameKindNumber) {
    return getHighestGroupRank(cards, sameKindNumber, null);
  }

  public static Rank getHighestGroupRank(final Set<Card> cards, final int sameKindNumber,
      final Rank excludedRank) {
    return groupByRank(cards).entrySet().stream()
        .filter(entry -> entry.getValue().intValue() >= sameKindNumber
            && !entry.getKey().equals(excludedRank))
        .map(Entry::getKey).max(Comparator.comparing(Rank::getValue))
        .orElseThrow(RuntimeException::new);
  }

  public static long getPairCount(final Set<Card> cards) {
    final EnumMap<Rank, Long> rankMap = groupByRank(cards);
    return rankMap.values().stream().filter(count -> count > 1).count();
  }

  private static EnumMap<Rank, Long> groupByRank(final Set<Card> cards) {
    return cards.stream()
        .collect(groupingBy(Card::getRank, () -> new EnumMap<>(Rank.class), counting()));
  }

  public static int getSameSuitCount(final Set<Card> cards) {
    Set<Card> sameSuitCards = getSameSuitCards(cards);
    if (sameSuitCards != null) {
      return sameSuitCards.size();
    }
    throw new RuntimeException();
  }

  public static Set<Card> getSameSuitCards(final Set<Card> cards) {
    final SameSuitCollector collector = cards.stream().collect(SameSuitCollector::new,
        SameSuitCollector::accept, SameSuitCollector::combine);

    return collector.getSameSuitCards();
  }

  public static long getMaxSameRankCount(final Set<Card> cards) {
    return groupByRank(cards).values().stream().mapToLong(Long::longValue).max()
        .orElseThrow(RuntimeException::new);
  }

  public static int getDifferentRankCount(final Set<Card> cards) {
    return groupByRank(cards).keySet().size();
  }

  public static Set<Card> getCardsInSequence(final Set<Card> cards) {
    final Set<Card> cardsInSequence =
        cards.stream().sorted(LOW_ACE_COMPARATOR).collect(toMaxCardsInSequenceAceLow());

    final boolean containsAce = getHighestCard(cards).getRank() == ACE;
    if (!containsAce || getDifferentRankCount(cardsInSequence) > 4) {
      return cardsInSequence;
    }

    return cards.stream().sorted(HIGH_ACE_COMPARATOR).collect(toMaxCardsInSequenceAceHigh());
  }

  private static Collector<Card, CardsInSequenceCollector, Set<Card>> toMaxCardsInSequenceAceLow() {
    return toMaxCardsInSequence((r1, r2) -> {
      final boolean isAce = r1 == ACE;
      return !isAce ? r1.isPredecessor(r2) : r2 == DEUCE;
    });
  }

  private static Collector<Card, CardsInSequenceCollector, Set<Card>> toMaxCardsInSequenceAceHigh() {
    return toMaxCardsInSequence(Rank::isPredecessor);
  }

  private static Collector<Card, CardsInSequenceCollector, Set<Card>> toMaxCardsInSequence(
      final BiFunction<Rank, Rank, Boolean> function) {
    return Collector.of(() -> new CardsInSequenceCollector(function),
        CardsInSequenceCollector::accept,
        (c1, c2) -> c1.getLongestSequence().size() > c2.getLongestSequence().size() ? c1 : c2,
        c -> c.cardsInSequence.size() > c.longestInSequence.size() ? c.cardsInSequence
            : c.longestInSequence);
  }

  private static class AceComparator implements Comparator<Card> {

    private final boolean useLowValue;

    public AceComparator() {
      this(true);
    }

    public AceComparator(final boolean useLowValue) {
      this.useLowValue = useLowValue;
    }

    @Override
    public int compare(final Card c1, final Card c2) {
      final Rank rank1 = c1.getRank();
      final Rank rank2 = c2.getRank();
      if (rank1 == ACE) {
        return useLowValue ? -1 : 1;
      }
      if (rank2 == ACE) {
        return useLowValue ? 1 : -1;
      }

      return rank1.compareTo(rank2);
    }
  }

  private static class SameSuitCollector implements Consumer<Card> {

    private final EnumMap<Suit, Set<Card>> sameSuitCards = new EnumMap<>(Suit.class);

    @Override
    public void accept(final Card card) {
      final Suit suit = card.getSuit();

      final Set<Card> cards = sameSuitCards.getOrDefault(suit, new HashSet<>());

      cards.add(card);
      sameSuitCards.put(suit, cards);
    }

    private void combine(final SameSuitCollector otherCalculator) {
      throw new UnsupportedOperationException("Not supported currently. " + otherCalculator);
    }

    private Set<Card> getSameSuitCards() {
      Set<Card> cards = null;
      for (final Set<Card> cardSet : sameSuitCards.values()) {
        if (cards == null || cardSet.size() > cards.size()) {
          cards = cardSet;
        }
      }

      return cards;
    }

    @Override
    public String toString() {
      return format("SameSuitCollector [sameSuitCards=%s]", sameSuitCards);
    }

  }

  private static class CardsInSequenceCollector {

    final Set<Card> longestInSequence = new LinkedHashSet<>();
    final Set<Card> cardsInSequence = new LinkedHashSet<>();
    private final BiFunction<Rank, Rank, Boolean> predecessorFunction;
    Card previousCard = null;

    public CardsInSequenceCollector(final BiFunction<Rank, Rank, Boolean> predecessorFunction) {
      this.predecessorFunction = predecessorFunction;
    }

    private void accept(final Card card) {
      if (previousCard == null) {
        cardsInSequence.add(card);
      } else {
        final Rank rank = previousCard.getRank();
        final Rank otherRank = card.getRank();

        if (predecessorFunction.apply(rank, otherRank) || rank.compareTo(otherRank) == 0) {
          cardsInSequence.add(card);
        } else if (!isStraight()) {
          if (longestInSequence.size() < cardsInSequence.size()) {
            longestInSequence.clear();
            longestInSequence.addAll(cardsInSequence);
          }
          cardsInSequence.clear();
          cardsInSequence.add(card);
        }
      }

      previousCard = card;
    }

    private Set<Card> getLongestSequence() {
      return cardsInSequence.size() > longestInSequence.size() ? cardsInSequence
          : longestInSequence;
    }

    private boolean isStraight() {
      return getDifferentRankCount(new HashSet<>(cardsInSequence)) > 4;
    }

  }

}
