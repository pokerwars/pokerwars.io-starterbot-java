package io.pokerwars.bot;

import io.pokerwars.bot.model.out.YourBot;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PokerwarsService {

  private static final Logger LOG = LoggerFactory.getLogger(PokerwarsService.class);

  private static final String POKERWARS_SERVER_PATH = "/v1/pokerwars";
  private static final String POKERWARS_SUBSCRIBE_PATH = "/subscribe";

  @Value("${pokerwars.bot.username}")
  private String yourBotUsername;

  @Value("${pokerwars.bot.token}")
  private String yourBotToken;

  @Value("${pokerwars.bot.endpoint}")
  private String yourBotEndpoint;

  @Value("${pokerwars.bot.notifications}")
  private Boolean notifications;

  @Value("${server.port}")
  private String yourBotPort;

  @Value("${pokerwars.server}")
  private String pokerwarsServer;

  private Client restClient;

  public PokerwarsService(Client restClient) {
    this.restClient = restClient;
  }

  public void subscribeBotToPokerwars() {
    String yourBotAddress = yourBotEndpoint + ":" + yourBotPort;
    YourBot yourBot = new YourBot(yourBotUsername, yourBotToken, yourBotAddress, notifications);
    Response pokerwarsResponse = callPokerwarsSubscribeEndpoint(yourBot);
    StatusType statusInfo = pokerwarsResponse.getStatusInfo();

    if (statusInfo.getFamily() == Family.SUCCESSFUL) {
      LOG.info("{} subscribed to pokerwars.io successfully!", yourBot);
      LOG.info("Your bot is now playing against other poker bots!");
      LOG.info("Check your stats at pokerwars.io/stats");
    } else {
      String reason = pokerwarsResponse.readEntity(String.class);
      int statusCode = statusInfo.getStatusCode();
      String reasonPhrase = statusInfo.getReasonPhrase();
      LOG.error("Your bot did NOT subscribe succesfully! Reason: {} - {}, {}", statusCode,
          reasonPhrase, reason);
      LOG.info("Fix the reported error and retry.");
      System.exit(-1);
    }
  }

  private Response callPokerwarsSubscribeEndpoint(YourBot yourBot) {
    LOG.info("Trying to subscribe your bot {} to pokerwars.io, calling {}{}{}", yourBot,
        pokerwarsServer, POKERWARS_SERVER_PATH, POKERWARS_SUBSCRIBE_PATH);
    return restClient.target(pokerwarsServer).path(POKERWARS_SERVER_PATH)
        .path(POKERWARS_SUBSCRIBE_PATH).request().post(Entity.json(yourBot));
  }

}
