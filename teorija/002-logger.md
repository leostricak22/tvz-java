# Java Logger

### Dodavanje loggera u projekt
- unutar pom.xml u ***dependencies*** dodati

    ```xml
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.5.11</version>
    </dependency>
    ```
- reloadati Maven project

### Konfiguracijska datoteka
- naziv datoteke: *logback.xml*
- datoteka se treba nalaziti u resources mapi
- ispisuje se datum, razina, thread, koji logger, koja datoteka, linija te poruka

    ```xml
    <configuration>
        <appender name="FILE" class="ch.qos.logback.core.FileAppender">
            <file>logs/pogreske.log</file>
            <encoder>
                <pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
            </encoder>
            </appender>
        <root level="debug">
            <appender-ref ref="FILE"/>
        </root>
    </configuration>
    ```
- u IntelliJ se treba napraviti "Reload from disk" da bi konfiguracijska datoteka funkcionirala

### Kreiranje logger objekta
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

...

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class); // koristiti klasu u kojoj koristimo logger
    ...
}
```

### Korištenje loggera
```java
logger.info("The application is started...");
logger.error("User entered invalid mail", exception); // ili bez exception
logger.warn(...);
logger.debug(...);
logger.trace(...); // vrijednosti varijabli, adrese,...
```

### Apache Maven
- alat za pojednostavljenje upravljanja ovisnostima o vanjskim bibliotekama
- omogućava kreiranje lokalnog repozitorija koji se automatski ažurira resursima iz globalnog repozitorija na internetu
- upravlja se iz datoteke *pom.xml*