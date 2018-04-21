package io.pokerwars.bot.model.in;

import java.util.List;
import java.util.Set;

public class GameInfo {

  public enum RoundTurn {
    PRE_FLOP, FLOP, TURN, RIVER, FINISHED;
  }

  private String yourUsername;
  private Long tournamentId;
  private int roundId;
  private Long smallBlindValue;
  private Long bigBlindValue;
  private String smallBlindPlayer;
  private String bigBlindPlayer;
  private RoundTurn roundTurn;
  private Set<Card> yourCards;
  private Set<Card> tableCards;
  private List<PlayerInfo> players;
  private Boolean canCheckOrBet;
  private Boolean canRaise;
  private Long minBet;
  private Long minRaise;
  private Long yourChips;
  private Long yourPot;

  public String getYourUsername() {
    return yourUsername;
  }

  public void setYourUsername(String yourUsername) {
    this.yourUsername = yourUsername;
  }

  public Long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(Long tournamentId) {
    this.tournamentId = tournamentId;
  }

  public int getRoundId() {
    return roundId;
  }

  public void setRoundId(int roundId) {
    this.roundId = roundId;
  }

  public Long getSmallBlindValue() {
    return smallBlindValue;
  }

  public void setSmallBlindValue(Long smallBlindValue) {
    this.smallBlindValue = smallBlindValue;
  }

  public Long getBigBlindValue() {
    return bigBlindValue;
  }

  public void setBigBlindValue(Long bigBlindValue) {
    this.bigBlindValue = bigBlindValue;
  }

  public String getSmallBlindPlayer() {
    return smallBlindPlayer;
  }

  public void setSmallBlindPlayer(String smallBlindPlayer) {
    this.smallBlindPlayer = smallBlindPlayer;
  }

  public String getBigBlindPlayer() {
    return bigBlindPlayer;
  }

  public void setBigBlindPlayer(String bigBlindPlayer) {
    this.bigBlindPlayer = bigBlindPlayer;
  }

  public RoundTurn getRoundTurn() {
    return roundTurn;
  }

  public void setRoundTurn(RoundTurn roundTurn) {
    this.roundTurn = roundTurn;
  }

  public Set<Card> getYourCards() {
    return yourCards;
  }

  public void setYourCards(Set<Card> yourCards) {
    this.yourCards = yourCards;
  }

  public Set<Card> getTableCards() {
    return tableCards;
  }

  public void setTableCards(Set<Card> tableCards) {
    this.tableCards = tableCards;
  }

  public List<PlayerInfo> getPlayers() {
    return players;
  }

  public void setPlayers(List<PlayerInfo> players) {
    this.players = players;
  }

  public Boolean canCheckOrBet() {
    return canCheckOrBet;
  }

  public void setCanCheckOrBet(Boolean canCheckOrBet) {
    this.canCheckOrBet = canCheckOrBet;
  }

  public Boolean canRaise() {
    return canRaise;
  }

  public void setCanRaise(Boolean canRaise) {
    this.canRaise = canRaise;
  }

  public Long getMinBet() {
    return minBet;
  }

  public void setMinBet(Long minBet) {
    this.minBet = minBet;
  }

  public Long getMinRaise() {
    return minRaise;
  }

  public void setMinRaise(Long minRaise) {
    this.minRaise = minRaise;
  }

  public Long getYourChips() {
    return yourChips;
  }

  public void setYourChips(Long yourChips) {
    this.yourChips = yourChips;
  }

  public Long getYourPot() {
    return yourPot;
  }

  public void setYourPot(Long yourPot) {
    this.yourPot = yourPot;
  }

  @Override
  public String toString() {
    return String.format(
        "GameInfo [yourUsername: %s, tournamentId: %s, roundId: %s, smallBlindValue: %s, bigBlindValue: %s, smallBlindPlayer: %s, bigBlindPlayer: %s, roundTurn: %s, yourCards: %s, tableCards: %s, players: %s, canCheckOrBet: %s, canRaise: %s, minBet: %s, minRaise: %s, yourChips: %s, yourPot: %s]",
        yourUsername, tournamentId, roundId, smallBlindValue, bigBlindValue, smallBlindPlayer,
        bigBlindPlayer, roundTurn, yourCards, tableCards, players, canCheckOrBet, canRaise, minBet,
        minRaise, yourChips, yourPot);
  }

}
