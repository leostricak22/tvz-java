# Java lambda izrazi

### Uvod
- uvode se u verziji Java 8
- zašto se koriste?
    - bez lambda izraza u radu s zbirkama je potrebno koristiti vanjsku iteraciju (external iteration) 
        
        ```java
        List<Student> students = ...
        double highestScore = 0.0;

        for(Student s : students) {
            if (s.getGradYear() == 2011) {
                if(s.getScore() > highestScore) {
                    highestScore = s.getScore();
                }
            }
        }
        ```
        - iteracija se kontrolira kroz programski kod i prolazi od početka do kraja zbirke
        - nije thread-safe
    - korištenjem lambda izraza taj kod izgleda ovako
        
        ```java
        double highestScore = students.stream()
            .filter(new Predicate<Student>() {
                public boolean op(Student s) {
                    return s.getGradYear() == 2011;
                }
            })
            .map(new Mapper<Student, Double>() {
                public Double extract(Student s) {
                    return s.getScore();
                }
            })
            .max();
        ```
        - iteracija se izvodi od strane libraryja
        - prolazak po elementima se može paralelizirati i u jednom prolazu
        - thread-safe
        - teže za razumijevanje i korištenje
        - navedeni programski izraz može se skratiti

            ```java
            double highestScore = students.stream()
                .filter(Student s -> s.getGradYear() == 2011)
                .map(Student s -> s.getScore())
                .max();
            ```

### Sintaksa lambda izraza
```java
(parameters) -> { lambda-body }
```
- tijelo lambde može baciti iznimku
- ako se tijelo sastoji samo od jedne linije, nije potrebno koristiti zagrade


### Funkcionalna sučelja
- sadrže samo jednu apstraktnu metodu

    ```java
    interface FileFilter        { boolean accept(File x); }
    interface ActionListener    { void actionPerformed(...); }
    interface Callable<T>       { T call(); }
    ```
- lambda izrazi predstavljaju implementaciju apstraktne metode
- važna sučelja
    - Predicate\<T\>
        - predstavlja *Boolean* vrijednost funkcije s jednim argumentom
        - sadrži korisne statičke metode poput *and()*, *or()*, *negate()* i *isEqual()*    

            ```java
            Student s -> s.graduationYear() == 20011

            Files.find(start, maxDepth,
                (path, attr) -> String.valueOf(path).endsWith(".js") &&
                                attr.size() > 1024,
                FileVisitOption.FOLLOW_LINKS);
            ```

    - Consumer\<T\>
        - operacija koja prima jednu vrijednost i ne vraća rezultat
        - omogućava ulančavanje funkcija korištenjem metode *andThen(Consumer after)*

            ```java
            String s -> System.out.println(s);
            (k, v) -> System.out.println("key: " + k + ", value: " + v);
            ```
    - Function\<T,R\>
        - prima jedan argument i vraća rezultat
        - može primati i vraćati različite tipove parametra
        - za ulančavanje *compose* i *andThen*

            ```java
            String s -> s.getName();
            (String name, Student s) -> new Teacher(name, student);
            ```
    - UnaryOperator\<T\>
        - specijalni oblik *Function* sučelja
        - prima jedan parametar i vraća isti tip parametra po principu `T apply(T a)`

            ```java
            String s -> s.toLowerCase();
            ```
    - BinaryOperator\<T\>
        - specijalni oblik *Function* sučelja
        - prima dva parametra i vraća isti tip parametra po principu `T apply(T a, T b)`

            ```java
            (String x, String y) -> {
                if(x.length > y.length) 
                    return x;

                return y;
            }
            ```
    - Supplier\<T\>


### Vrijednosti u lambda izrazima
- parametri lambda funkcije se ponašaju kao da imaju *final* modifikator
- može se zanemariti, ali se podrazumijeva
- u primjeru varijabli *name* je implicitno dodijeljen modifikator *final*

    ```java
    String name = getUserName();
    button.addActionListener(event -> System.out.println("hi " + name));
    ```
- varijabla `name` je *effectively  final* i podrazumijeva se da se varijabli samo jednom može dodijeliti vrijednost ako se koristi u lambda izrazu
- ako se pokuša prevesti sljedeći kod, kompajler vraća vrijednost

    ```java
    String name = getUserName();
    name = formatUserName(name);
    button.addActionListener(event -> System.out.println("hi " + name));
    ```

### Korištenje tipova parametara
- često se mogu izostavljati zbog čitljivosti
    - ponekad ih je bolje pisati iako nije nužno da bi kod bio razumljiviji
- mogućnost izostavljanja parametara je uvedena od Jave 7 korištenjem *diamond operatora*
- primjer

    ```java
    // funkcijsko sučelje Predicate izgleda ovako
    public interface Predicate<T> { boolean test(T t); }
    
    // može se koristiti na sljedeće načine
    Predicate<Integer> atLeast5 = x -> x >= 5;
    Predicate<Integer> atLeast5 = (Integer x) -> x >= 5;
    ```

### Optional
- predstavlja novi tip podataka koji predstavlja bolju alternativu od *null* vrijednosti
    - vrijednost koja može postojati ili ne
- često korisna kada *filter* u *Streamu* ne vrati nikakvu vrijednost

    ```java
    Optional<String> a = Optional.of("a");

    Optional<String> emptyOptional = Optional.empty();
    Optional<String> alsoEmpty = Optional.ofNullable(null);

    System.out.println(a.get() + "\n" + emptyOptional + "\n" + alsoEmpty);
    ```
    ```
    a
    Optional.empty
    Optional.empty
    ```
- omogućuje izbjegavanje problema s iznimkom *NullPointerException*
- jednostavnije se može koristiti i ovako

    ```java
    // ovo
    if(x != null)
        print(x)

    // mjenjamo sa sljedećim
    opt.isPresent(x -> print(x))
    ```
- u *Streamu* potrebno koristiti *flatMap* za dobivanje Optional tipa

### Java 9 proširenja Optional tipa
- moguće je koristiti referencu na metodu *Optional::stream* ili još kraće

    ```java
    public Stream<Customer> findCustomers(Collection<String> customerIds) {
        return customerIds.stream()
            .map(this::findCustomer)
            .flatMap(Optional::stream)
    }

    public Stream<Customer> findCustomers(Collection<String> customerIds) {
        return customerIds.stream()
            .flatMap(id -> findCustomer(id).stream());
    }
    ```
- moguće je vratiti listu vrijednosti po zadanom kriteriju

    ```java
    public List<Order> findOrdersForCustomer(String customerId) {
        return findCustomer(customerId)
            .map(this::getOrders)
            .orElse(new ArrayList<>());
    }
    public Stream<Order> findOrdersForCustomer(String customerId) {
        return findCustomer(customerId)
            .stream()
            .map(this::getOrders)
            .flatMap(List::stream);
    }
    ```
- metodom *Optional::or* može se definirati više kriterija (potrebno koristiti povratni tip Optional)

    ```java
    public Optional<Customer> findCustomer(String customerId) {
        return customers.findInMemory(customerId)
            .or(() -> customers.findOnDisk(customerId))
            .or(() -> customers.findRemotely(customerId));
    }
    ```
- dodane metode *ifPresentOrElse*, *ifPresent* i *ifEmpty*

    ```java
    public void logLogin(String customerId) {
        findCustomer(customerId)
            .ifPresentOrElse(
            this::logLogin,
            () -> logUnknownLogin(customerId)
        );
    }

    public void logLogin(String customerId) {
        findCustomer(customerId)
            .ifPresent(this::logLogin)
            .ifEmpty(() -> logUnknownLogin(customerId));
    }
    ```

### Reference za metode
- omogućavaju ponovno iskorištavanje metoda kao lambda izraza

    ```java
    // ovo
    FileFilter x = File f -> f.canRead();

    // postaje 
    FileFilter x = File::canRead;
    ```
- pišu se po principu `<referenca_objekta>::<naziv_metode>`
- mogu biti statičke metode, metode klasa ili metode objekta