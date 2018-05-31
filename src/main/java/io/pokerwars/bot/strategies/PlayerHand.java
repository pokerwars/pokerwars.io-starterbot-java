package io.pokerwars.bot.strategies;

import io.pokerwars.bot.model.in.Card;

import java.util.HashSet;
import java.util.Set;

public class PlayerHand {

  private final Set<Card> cards = new HashSet<>();

  public PlayerHand(final Set<Card> handCards, final Set<Card> tableCards) {
    cards.addAll(handCards);
    cards.addAll(tableCards);
  }

  public Set<Card> getCards() {
    return cards;
  }

}