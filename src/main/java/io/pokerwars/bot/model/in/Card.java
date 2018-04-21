package io.pokerwars.bot.model.in;

public class Card {

  public enum Rank {
    DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
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
  public String toString() {
    return String.format("%s of %s", getRank(), getSuit());
  }

}
