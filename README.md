# Orianna V2

A Java adaptation of the Riot Games LoL API (http://developer.riotgames.com/).

Orianna is back, rebuilt from the ground up with some new features, Java 7 compatibility, and a whole lot more in store.
In addition to the standard RiotAPI we've added a BaseRiotAPI which can interact with the Riot API exactly according to specification.
We've also changed the core architecture to be more resilient to future API changes and ease maintenance. 
Expect cool new features like automatic generation of local databases (through the same old interface) coming down the pipe soon!

## Features (RiotAPI)

- Usability-focused type system to make your life easy
- Automatically throttles requests to fit rate limits
- Ensures well-formed API requests
- Replaces foreign key ID values with the referenced object
- Option to lazy load references objects right when you need them or load them automatically upfront with minimal API calls
- Caches static data and summoner information to accelerate access and reduce API load

## Features (BaseRiotAPI)

- Meets the Riot API specification exactly
- Automatically throttles requests to fit rate limits
- Ensures well-formed API requests
- Make only the requests you want to make, no foreign keys are auto-filled
 
## Setup

Just [download](https://github.com/robrua/Orianna/releases) the latest .jar and add it to your project's build path.
 
To do this in eclipse, I recommend creating a lib/ directory in your project's root directory and putting the .jar there. Then just right click the .jar in eclipse and click Build Path -> Add to Build Path.

If you use Maven to manage your dependencies, Orianna is posted on Maven Central @ com.robrua.orianna

## Dependencies

Orianna relies on [Apache HttpClient](http://hc.apache.org/httpcomponents-client-ga/) v4.3.5 and [Google GSON](https://code.google.com/p/google-gson/) v2.3.1. Both are included in the JARs distributed here.
 
## Usage

Here's some examples of a few basic uses of the API. The full JavaDoc can be found at http://robrua.github.io/Orianna/.

```java
import java.util.List;

import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.QueueType;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.league.League;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.summoner.Summoner;

public class Example {
    public static void main(String[] args) {
        RiotAPI.setMirror(Region.NA);
        RiotAPI.setRegion(Region.NA);
        RiotAPI.setAPIKey("YOUR-API-KEY-HERE");
        
        Summoner summoner = RiotAPI.getSummonerByName("FatalElement");
        System.out.println(summoner.getName() + " is a level " + summoner.getLevel() + " summoner on the NA server.");
        
        List<Champion> champions = RiotAPI.getChampions();
        System.out.println("He enjoys playing LoL on all different champions, like " + champions.get((int)(champions.size() * Math.random())) + ".");
        
        League challenger = RiotAPI.getChallenger(QueueType.RANKED_SOLO_5x5);
        Summoner bestNA = challenger.getEntries().get(0).getSummoner();
        System.out.println("He's much better at writing Java code than he is a LoL. He'll never be as good as " + bestNA + ".");
    }
}
```

Make sure you set your rate limit! Orianna will limit you the the default development limit until you give it your production limit (if you have one).

```java
// 10,000 calls per 10 seconds
RiotAPI.setRateLimit(10000, 10);
// 10,000 calls per 10 seconds AND 50,000 calls per minute
RiotAPI.setRateLimit(new RateLimit(10000, 10), new RateLimit(50000, 60));
```

New in V2, you can set a load policy for filling in foreign key values. UPFRONT will load everything ASAP with a minimal number of calls. LAZY will load things as you ask for them, so you can save calls if you don't use some values, but it won't be able to take as much advantage of bulk loading.

```java
// Upfront loading is the default strategy
RiotAPI.setLoadPolicy(LoadPolicy.UPFRONT);
```

Or, if you don't want all the bells and whistles and you'd just like to access the Riot API as the specification says, you can use BaseRiotAPI.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.robrua.orianna.api.dto.BaseRiotAPI;
import com.robrua.orianna.type.core.common.QueueType;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.dto.league.League;
import com.robrua.orianna.type.dto.staticdata.Champion;
import com.robrua.orianna.type.dto.staticdata.ChampionList;
import com.robrua.orianna.type.dto.summoner.Summoner;

public class Example {
    public static void main(String[] args) {
        BaseRiotAPI.setMirror(Region.NA);
        BaseRiotAPI.setRegion(Region.NA);
        BaseRiotAPI.setAPIKey("YOUR-API-KEY-HERE");
        
        Map<String, Summoner> summoners = BaseRiotAPI.getSummonersByName("FatalElement");
        Summoner summoner = summoners.get("fatalelement");
        System.out.println(summoner.getName() + " is a level " + summoner.getSummonerLevel() + " summoner on the NA server.");
        
        ChampionList champs = BaseRiotAPI.getChampions();
        List<Champion> champions = new ArrayList<>(champs.getData().values());
        System.out.println("He enjoys playing LoL on all different champions, like " + champions.get((int)(champions.size() * Math.random())).getName() + ".");
        
        League challenger = BaseRiotAPI.getChallenger(QueueType.RANKED_SOLO_5x5);
        String aChallenger = challenger.getEntries().get(0).getPlayerOrTeamName();
        System.out.println("He's much better at writing Java code than he is a LoL. He'll never be as good as " + aChallenger + ".");
    }
}
```

## JavaDoc
[Found Here](http://robrua.github.io/Orianna/)

## Download
[Check Releases](https://github.com/robrua/Orianna/releases)

## Questions/Contributions
Feel free to send pull requests or to contact me via github or email (robrua@alumni.cmu.edu).

## Bugs
There's probably typos or some data missing somewhere in the project. Let me know about any of them you run into. I'm also looking for consistent maintainers to help me out as the Riot API evolves.

## Disclaimer
This product is not endorsed, certified or otherwise approved in any way by Riot Games, Inc. or any of its affiliates.