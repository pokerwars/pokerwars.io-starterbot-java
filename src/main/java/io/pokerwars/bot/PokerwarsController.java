package io.pokerwars.bot;

import io.pokerwars.bot.model.in.GameInfo;
import io.pokerwars.bot.model.in.Notification;
import io.pokerwars.bot.model.out.Pong;
import io.pokerwars.bot.model.out.actions.PokerAction;
import io.pokerwars.bot.strategies.PokerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokerwars.io")
public class PokerwarsController {

  private static final Logger LOG = LoggerFactory.getLogger(PokerwarsController.class);

  // you can select your strategy in the application.yml file
  @Value("${pokerwars.bot.strategy}")
  private PokerStrategy pokerStrategy;

  private PokerwarsService pokerwarsService;

  public PokerwarsController(PokerwarsService pokerwarsService) {
    this.pokerwarsService = pokerwarsService;
  }

  @PostMapping("play")
  public ResponseEntity<PokerAction> play(@RequestBody GameInfo gameInfo) {
    // This endpoint is called by pokerwars.io to request your bot next move on a tournament.
    // You have the current state of the table in the GameInfo object, which you can use to decide
    // your next move.
    LOG.info(
        "Game info received for tournament {} and round {}, let's decide the next bot move for this hand",
        gameInfo.getTournamentId(), gameInfo.getRoundId());

    LOG.info("Current round turn is {}", gameInfo.getRoundTurn());
    LOG.info("Cards on the table are {}", gameInfo.getTableCards());
    LOG.info("Your bot cards are {}", gameInfo.getYourCards());

    if (gameInfo.canCheckOrBet()) {
      // remember: in poker you can check or bet only if in the current turn no bot has bet already
      // if a bot bet already, you'll need to call or raise.
      LOG.info("In this hand, your bot can check or bet. The minimum bet is {}",
          gameInfo.getMinBet());
    }

    if (gameInfo.canRaise()) {
      // remember: in poker you can raise only if there has been a bet before
      LOG.info("In this hand, your bot can raise. The minimum raise is {}", gameInfo.getMinRaise());
    }

    LOG.info("The value of small blind now is {}", gameInfo.getSmallBlindValue());
    LOG.info("The value of big blind now is {}", gameInfo.getBigBlindValue());

    LOG.info("Small blind player is {}", gameInfo.getSmallBlindPlayer());
    LOG.info("Big blind player is {}", gameInfo.getBigBlindPlayer());
    LOG.info("Players in turn order with their info are: {}", gameInfo.getPlayers());

    // get the next move based on the strategy you selected in the application.yml
    LOG.info("Your bot is using strategy {}", pokerStrategy);
    PokerAction nextMove = pokerStrategy.play(gameInfo);
    LOG.info("Your bot next move is {}", nextMove);

    return ResponseEntity.ok(nextMove);
  }

  @PostMapping("notifications")
  @ResponseStatus(HttpStatus.OK)
  public void notifications(@RequestBody Notification notification) {
    String type = notification.getType();
    String message = notification.getMessage();
    LOG.info("Received notification of type {} with message: {}", type, message);

    if (type.toLowerCase().contains("wrong")) {
      LOG.error("Your bot has an issue. Check the error and fix it.");
      System.exit(-1);
    }
  }

  @GetMapping("subscribe")
  @ResponseStatus(HttpStatus.OK)
  public void subscribe() {
    // This endpoint can be used by pokerwars.io to resubscribe your bot in exceptional situations
    LOG.info("Trying to subscribe to pokerwars.io...");
    try {
      pokerwarsService.subscribeBotToPokerwars();
      LOG.info("Subscribed successfully!");
    } catch (Exception e) {
      LOG.error("Unable to subscribe to pokerwars.io :( check the stacktrace for more info", e);
      System.exit(-1);
    }
  }

  @GetMapping("ping")
  public ResponseEntity<Pong> ping() {
    // This is used by pokerwars.io when your bot subscribe to verify that is alive and responding
    LOG.info("Received ping from pokerwars.io, responding with a pong");
    return ResponseEntity.ok(new Pong());
  }

}
