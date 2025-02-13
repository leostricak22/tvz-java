# Niti

# Uvod
- ***višenitnost (multithreading)*** se kod programiranja koristi u slučaju kad je potrebno istovremeno paralelno izvršavanje više zadataka
- paralelno izvođenje zadataka podrazumijeva simultano izvođenje zadataka
    - moguće u slučaju višejezgrenih procesora
- u Javi je moguće koristiti niti uz pomoć API-a koji je ugrađen u jezik
- u Javi svaka nit ima ***vlastiti stog*** za pozivanje metoda (method-call stack) i ***vlastito programsko brojilo*** (program counter)
- time je omogućeno da niti međusobno mogu dijeliti resurse poput memorije ili datoteke
- potrebno je pripaziti na sinkronizaciju među nitima
- ***deadlock*** nastaje ako nit čeka resurs koji druga nit nikad neće osloboditi
- životni ciklus niti 
    - ***new***
        - ***runnable*** (zajedno s new započinje životni ciklus)
            - ***waiting*** (vraća se u runnable)
                - čeka da neka druga nit obavi zadatak
                - ne može se koristiti procesor tako dugo dok je druga nit ne "probudi"
            - ***timed waiting***  (vraća se u runnable)
                - čeka zadani vremenski interval
                - ne može se koristiti procesor tako dugo dok interval ne prođe
            - ***terminated*** (nit je gotova, treba se pokrenuti novi thread i ne može se prebaciti u drugo stanje)
                - završava životni ciklus
                    - ako je nit gotova
                    - ako se dogodila greška
            - ***blocked***  (vraća se u runnable)
                - čekamo npr. da se oslobode neki resurse koje koriste druge niti
    - operacijski sustav bira trenutak kad će se izvesti nit (***thread scheduler***)
    - moguće postavljanje prioriteta

# Sučelje Runnable
- za implementaciju niti nužno je implementirati sučelje ***Runnable***
    - sadrži jednu metodu, ***run***
    - ona sadrži logiku koja se izvodi kada je nit aktivna (u stanju Runnable)
- objekti koje predstavljaju niti moraju se kreirati i pokrenuti uz pomoć Execution Frameworka
- korištenjem statičkih metoda iz klase ***Thread*** moguće je prebaciti nit u stanje čekanja ili prekinuti izvođenje niti
    ```java
    Thread.sleep(); // stavlja se u stanje čekanja
    Thread.interrupt(); // prekida se izvođenje niti
    ```
    ```java
    // niti trebaju implementirati Runnable
    public class PrintTask implements Runnable {
        
        private final static SecureRandom generator = new SecureRandom();
        private final int sleepTime;
        private final String taskName;

        public PrintTask(String taskName) {
            this.taskName = taskName;
            sleepTime = generator.nextInt(5000);
        }

        // run metoda se izvršava kada se pokrene nit
        @Override
        public void run() {
            try {
                System.out.printf("%s going to sleep for %d milliseconds.%n", taskName, sleepTime);
                Thread.sleep(sleepTime); // na spavanju ju neka druga nit može prekinuti
            } catch (InterruptedException exception) { // baca InterruptedException
                exception.printStackTrace();
                Thread.currentThread().interrupt();
            }

            System.out.printf("%s done sleeping%n", taskName);
        }
    }
    ```

# Kreiranje i izvršavanje niti korištenjem Execution Frameworka
- za izvođenje niti u Javi koristi se Execution Framework koja uz pomoć Executor objekta pokreće izvršavanje ***run*** metoda u objektima klase koje implementiraju sučelje ***Runnable***
- brine se i za kreiranje grupa niti koja se često naziva i ***thread pool***
- sadrži jednu metodu ***execute*** koja prima Runnable tipove objekata
- Executor dodjeljuje svakom Runnable objektu jednu od slobodnih niti iz thread poola
    ```java
    public class TaskExecutor {

        public static void main(String[] args) {
            // stvaranje niti
            PrintTask task1 = new PrintTask("task1");
            PrintTask task2 = new PrintTask("task2");
            PrintTask task3 = new PrintTask("task3");

            System.out.println("Starting Executor");

            // stvaranje thread poola
            ExecutorService executorService = Executors.newCachedThreadPool();

            // pokretanje niti
            executorService.execute(task1);
            executorService.execute(task2);
            executorService.execute(task3);

            // zatvaranje thread poola
            executorService.shutdown();

            System.out.println("Tasks started, main ends.%n");
        }
    }

    /*  ISPIS
        Starting Executor
        Tasks started, main ends.%n
        task1 going to sleep for 4874 milliseconds.
        task3 going to sleep for 2145 milliseconds.
        task2 going to sleep for 561 milliseconds.
        task2 done sleeping
        task3 done sleeping
        task1 done sleeping
    */
    ```

# Sinkronizacija niti
- kad više niti dijele isti objekt, može doći do neočekivanih vrijednosti
    - u takvim situacijama je potrebno koristiti mehanizme sinkronizacije koji omogućavaju kreiranje kritičnih isječaka u kojima se u nekom trenutku može nalaziti samo jedna nit koja se izvodi
    - da bi nit mogla ući u kritični isječak programskog koda, mora zauzeti ***monitor lock***
- u Javi se kritični isječci označavaju ključnom riječju ***synchronized***
- metoda ***awaitTermination*** iz objekta ***ExecutorService*** se koristi za čekanje u glavnom da završe sve niti u glavnom programu

# Metode za sinkronizaciju niti
- kad nit izlazi iz kritičnog odsječka, druge čekaju "signal" da se one mogu "natjecati" i ući
- klasa Object sadrži sljedeće metode za tu implementaciju
    - ***wait***
        - koristi se u slučaju kad nit uđe u kritični isječak i nakon toga ustanovi da joj je za nastavak rada na zadatku treba biti ispunjen neki od uvjeta
        - tada se otpušta ***monitor lock*** i nit ulazi u ***Waiting*** stanje
    - ***notify***
        - služi za obavještavanje da se nit iz stanja ***Waiting*** vraća u stanje ***Runnable***
    - ***notifyAll***
        - sve niti u ***Waiting*** stanju prelaze u ***Runnable*** stanje
    
# Primjer paralelnog izvođenja 3 niti
- GledanjeTVPrograma.java
    ```java
    public class GledanjeTVPrograma {

        private static final int BROJ_NITI = 3;

        public static void main(String[] args) {
            TVProgramRunnableNit prvaNit = new TVProgramRunnableNit("Utakmica Real - Barcelona");
            TVProgramRunnableNit drugaNit = new TVProgramRunnableNit("Turska sapunica");
            TVProgramRunnableNit trecaNit = new TVProgramRunnableNit("Dokumentarac");

            Thread thread1 = new Thread(prvaNit);
            Thread thread2 = new Thread(drugaNit);
            Thread thread3 = new Thread(trecaNit);

            thread1.start();
            thread2.start();
            thread3.start();

            // ili koristiti ExecutorService

            ExecutorService executorService = Executors.newFixedThreadPool(BROJ_NITI);
            executorService.execute(prvaNit);
            executorService.execute(drugaNit);
            executorService.execute(trecaNit);

            executorService.shutdown();
        }
    }
    ```
- TVProgramRunnableNit
    ```java
    public class TVProgramRunnableNit implements Runnable {

        private Thread nit;

        public TVProgramRunnableNit(String nazivPrograma) {
            nit = new Thread(this, nazivPrograma);
        }

        public void start() {
            nit.start();
        }

        @Override
        public void run() {
            for (int i=1; i <= 3; i++) {
                System.out.println("Prebačeno na program '" + Thread.currentThread().getName()
                        + "' " + i + ". put.");

                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Završen program '" + Thread.currentThread().getName() + "'!");
        }
    }
    ```