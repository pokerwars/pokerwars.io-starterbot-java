package io.pokerwars.bot.model.in;

import com.google.common.base.MoreObjects;
import java.util.Objects;

public class Card implements Comparable<Card> {

  public enum Rank {

    DEUCE(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(1);

    private int value;

    Rank(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public boolean isPredecessor(final Rank otherRank) {
      return otherRank.getValue() == value + 1;
    }
  }

  public enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
  }

  private Rank rank;
  private Suit suit;

  public Rank getRank() {
    return rank;
  }

  public void setRank(Rank rank) {
    this.rank = rank;
  }

  public Suit getSuit() {
    return suit;
  }

  public void setSuit(Suit suit) {
    this.suit = suit;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + Objects.hashCode(rank);
    result = 31 * result + Objects.hashCode(suit);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Card other = (Card) obj;
    return Objects.equals(rank, other.rank) && Objects.equals(suit, other.suit);
  }

  @Override
  public int compareTo(final Card otherCard) {
    return otherCard.getRank().compareTo(getRank());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("rank", rank)
        .add("suit", suit)
        .toString();
  }
}