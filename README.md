
# pokerwars.io-starterbot-java
This is a simple example of how you could implement a pokerwars.io bot with Java and [Spring Boot](https://projects.spring.io/spring-boot/). It comes with a few simple poker strategies, the default is `RANDOM`, but you should change this and try to implement your own!

## Quick start
A few requirements to play:
- have [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads) or newer installed
- make sure that the computer where your bot runs is visible from the internet, so we can communicate with him/her. [This is an useful service](http://canyouseeme.org/) to double check this. Bot default port is `3000`, but you can change this on the `src/main/resources/application.yml` file. If you need help to open a port on your router [check this guide](https://www.noip.com/support/knowledgebase/general-port-forwarding-guide/), or if you do not have access to your router or your bot is behind a firewall, try [ngrok](https://ngrok.com/). If you struggle, [contact us](mailto:contact@pokerwars.io), we are always willing to help you.
- [Register with us](https://www.pokerwars.io/) and retrieve your [API token](https://www.pokerwars.io/token) and [username](https://www.pokerwars.io/profile).
- check out this repo with git or download it from [this link](https://github.com/pokerwars/pokerwars.io-starterbot-java/archive/master.zip).
- in the code you just downloaded, update the `src/main/resources/application.yml` file with your username, token and bot ip address and port:
```
pokerwars:
    bot:
        username: [[[INSERT-YOUR-POKERWARS-USERNAME-HERE]]]
        token: [[[INSERT-YOUR-POKERWARS-TOKEN-HERE]]]
        # make sure your bot can be seen from external at the IP and port you specify below, so pokerwars.io can talk to him/her.
        # this is an useful service to double check this: http://canyouseeme.org/ . Bot default port is `3000`, but you can change this just above in this file
        endpoint: http://[[[INSERT-YOUR-PUBLICLY-AVAILABLE-BOT-IP-ADDRESS-AND-PORT-HERE]]] # example: http://1.2.3.4:3000/ 
```

Now you can implement your own poker strategy and play with it! Or use one of the available strategy.

## Play!
Now you are ready to run the bot!

If you are in Linux or Mac, open a console and from the main project directory type:
```
[pokerwars.io-starterbot-java]$ ./gradlew bootRun
```
If you are on Windows, open a command prompt and from the main project directory type:
```
pokerwars.io-starterbot-java> gradlew bootRun
```

The bot will try to subscribe to pokerwars.io when it starts up. If no errors happens, it will start playing straightaway, otherwise you should see an error. The most common is that we cannot see your bot, please double check [your bot is visible from the internet](http://canyouseeme.org/) and [you have configured your router correctly](https://www.noip.com/support/knowledgebase/general-port-forwarding-guide/). If you do not have access to your router or your bot is behind a firewall, try [ngrok](https://ngrok.com/).

### Playing on Glitch.com?
It's easy:

- Import this repo into a new Glitch project
- Check the Live App URL for your project under `Share` (it will look like https://<MY_POKERWARS_BOT>.glitch.me)
- Update your application.yml file as described above
- Start remixing!

Have fun!

## Extending this bot
If something is not clear in this code, refer to also to [our documentation](https://www.pokerwars.io/docs) for the structure and content of the data we send to your bot. How you choose to manipulate that information to inform your bot's strategy is up to you!
