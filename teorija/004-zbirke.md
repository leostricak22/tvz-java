# Zbirke podataka u Javi

### Zbirke
- predstavljaju podatkovnu strukturu u obliku strukture (Collection)
- sučelja
    - Collection
        - osnovno sučelje u hijerarhiji zbirki iz kojeg su naslijeđene zbirke Set i List
    - Set
        - sučelje zbirke koja ne može sadržavati duplikate
    - List
        - sučelje zbirke koja može sadržavati duplikate i čuva poredak elemenata
    - Map
        - sučelje zbirke koja pohranjuje parove "ključ-vrijednost", pri čemu ključevi ne mogu imate duplikate te **ne** naslijeđuje sučelje *Collection*
- ne može sadržavati primitivne tipove podataka
    - automatski se obavlja operacija "autoboxing" koja ih pretvara u referentne tipove

### Sučelje Collection i klasa Collections
- *Collection* sadrži skupne operacije koje se izvode nad svim objektima unutar zbirke
    - udruživanje, brisanje, uspoređivanje objekata ili elemenata zbirke
- zbirka iz skupine *Collection* također se može konvertirati u polje `collection.toArray()`
- *Collection* sadrži i metodu koja vraća *Iterator* objekt koji omogućava prolazak po svim elementima zbirke
- klasa *Collections* sadrži niz statičkih metoda koje omogućavaju pretraživanje, sortiranje i druge operacije nad elementima zbirke
    - uključuje i "wrapper" metode koje omogućavaju "sinkroniziranje" operacija nad elementima zbirke koje se koriste u višenitnom okruženju

### Liste
- List predstavlja sučelje, a najčešće korištene implementacije su ArrayList, LinkedList i Vector
- klase ArrayList i Vector imaju strukturu polja čija veličina je promjenjiva
    - brzo lociranje predmeta, ali ne omogućavaju dobre performanse u slučaju kod umetanja elemenata usred liste
    - osnovna razlika je u tome da su operacije unutar zbirke Vector sinkronizirane (može se koristiti u višenitnom okruženju), a ArrayList nije (ali ima bolje performanse)
- klasa LinkedList ima bolje performanse kod umetanja elemenata usred liste, ali kod pretraživanja elemenata mora slijedno prolaziti po svim elementima
- instanciranje lista

    ```java
    List<String> listaStringova1 = new ArrayList<>();
    List<String> listaStringova2 = new Vector<>();
    List<String> listaStringova3 = new LinkedList<>();
    ```
- od Jave SE 7 nije nužno ponavljanje tipa podataka koji se nalazi unutar liste
    - prije: new Vector<String>();
    - sada:  new Vector<>();
- za dohvat podataka koristi se foreach petlja (uvedena u Java SE 5) i iterator
- elementi se dodaju metodom *add*, a dohvaćaju metodom *get*
- primjer korištenja ArrayList

    ```java
    String[] poljeStringova = {"Proljeće", "Ljeto", "Jesen", "Zima", "Zima"};
    List<String> listaStringova = new ArrayList<>();

    // foreach
    for (String doba : poljeStringova)
        listaStringova.add(doba);

    Iterator<String> iterator = listaStringova.iterator();

    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
    ```
    ```
    Proljeće
    Ljeto
    Jesen
    Zima
    Zima
    ```
- primjer korištenja LinkedList

    ```java
    String[] poljeStringova = {"Proljeće", "Ljeto", "Jesen", "Zima", "Zima"};
    List<String> listaStringova = new LinkedList<>();

    for (String doba : poljeStringova)
        listaStringova.add(doba);

    for (String doba : listaStringova)
        System.out.println(doba);

    System.out.println();

    // vraća i briše prvi objekt iz liste
    String pop = ((LinkedList<String>) listaStringova).pop(); // trebamo navesti da je objekt instanca klase LinkedList<String>
    System.out.println(pop);

    String prvi = listaStringova.getFirst();
    System.out.println(prvi);
    ```
    ```
    Proljeće
    Ljeto
    Jesen
    Zima
    Zima

    Proljeće
    Ljeto
    ```

### Collections
- sadrži niz statičkih metoda koje uključuju algoritme visokih performansi za rad s elementima liste
- metode
    - sort
        - sortira elemente liste
    - binarySearch
        - koristi algoritam binarnog pretraživanja za pronalazak određenog elementa
    - reverse
        - obrnuto sortira elemente u listi
    - shuffle
        - miješa elemente prema slučajnom redosljedu
    - fill
        - popunjava zbirku
    - copy
        - kopira element jedne zbirke u drugu
    - min
        - vraća najmanji element iz zbirke
    - max
        - vraća najveći element iz zbirke
    - addAll
        - dodaje elemente iz jedne zbirke u drugu
    - frequency
        - koliko se puta neki objekt pojavljuje unutar zbirke
    - disjoint
        - provjerava sadrže li dvije zbirke iste objekte

    ```java
    String[] poljeStringova = {"Proljeće", "Ljeto", "Jesen", "Zima", "Zima"};
    List<String> listaStringova = Arrays.asList(poljeStringova);
    System.out.println(listaStringova);

    Collections.sort(listaStringova);   // sortiranje liste
    System.out.println(listaStringova);

    int broj = Collections.frequency(listaStringova, "Zima"); // Broj pojavljivanja u listi
    System.out.println(broj);
    ```
    ```
    [Proljeće, Ljeto, Jesen, Zima, Zima]
    [Jesen, Ljeto, Proljeće, Zima, Zima]
    2
    ```

### Setovi
- zbirka koja ne čuva poredak elemenata i sadržava samo po jednu referencu određenog objekta
- umetanjem elemenata koji već postoji zamjenjuje se stari element iz seta
- elementi se dodaju metodom *add*, a kroz njega se prolazi iteratorom ili foreach petljom slučajnim redosljedom
- najčešće implementacije su **HashSet** i **TreeSet**
- osim sučelja **Set** postoji i sučelje **SortedSet** koje omogućava sortiranje elemenata i sadrži metode: *first* i *last*
- **TreeSet** osim sučelja Set implementira i **SortedSet**

    ```java
    Set<String> setStringova = new HashSet<>();
    ```
- primjer korištenja HashSet

    ```java
    String[] poljeStringova = {"Proljeće", "Ljeto", "Jesen", "Zima", "Zima"};
    Set<String> setStringova = new HashSet<>();

    for (String doba : poljeStringova)
        setStringova.add(doba);

    for (String doba : setStringova)
        System.out.println(doba);
    ```
    ```
    Ljeto
    Jesen
    Zima
    Proljeće
    ```

- primjer korištenja SortedSet i TreeSet

    ```java
    Integer[] numbersArray = {11, 10, 2, 6, 8, 50, 22};
    SortedSet<Integer> sortedSet = new TreeSet<>(new HighestIntegerComparator());

    Collections.addAll(sortedSet, numbersArray);

    for (Integer integer : sortedSet) {
        System.out.println(integer);
    }
    ```
    ```
    50
    22
    11
    10
    8
    6
    2
    ```

### Mape
- zbirke koje povezuju ključeve s vrijednostima
- ključevi u mapi moraju biti *jedinstveni*, a vrijednosti ne
- najčešće implementacije
    - **Hashtable**
    - **HashMap**
    - **TreeMap**
- klase Hashtable i HashMap za spremanje podataka koriste hash tablicu, a TreeMap koristi strukturu stabla
- *TreeMap* implementira sučelje **SortedMap**

    ```java
    Map<Integer, Integer> mapaUnesenihBrojeva = new HashMap<>();
    ```
- primjer korištenja HashMap implementacije

    ```java
    Map<Integer, Integer> mapaUnesenihBrojeva = new HashMap<>();
    Scanner unos = new Scanner(System.in);

    for(int i = 0; i < 10; i++) {
        System.out.print("Unesite broj: ");
        Integer broj = unos.nextInt();

        if(mapaUnesenihBrojeva.containsKey(broj)) {             // provjera ako u mapi postoji ključ
            Integer kolicina = mapaUnesenihBrojeva.get(broj);
            kolicina += 1;
            mapaUnesenihBrojeva.put(broj, kolicina);            // dodavanje elementa gdje je ključ
        }
        else
            mapaUnesenihBrojeva.put(broj, 1);
    }

    System.out.println(mapaUnesenihBrojeva);

    unos.close();
    ```
    ```
    Unesite broj: 1
    Unesite broj: 2
    Unesite broj: 3
    Unesite broj: 4
    Unesite broj: 1
    Unesite broj: 2
    Unesite broj: 3
    Unesite broj: 4
    Unesite broj: 1
    Unesite broj: 2
    {1=3, 2=3, 3=2, 4=2}
    ```
- metodom *containsKey* moguće je provjeriti sadrži li mapa traženi ključ pa tek nakon toga dodavati nove elemente, ako ključ ne postoji
- dodavanjem ključa koji već postoji u mapi **izbacuje se stara vrijednost** i **dodaje nova**
    - ne dolazi do greške
- postoji i metoda *keySet* koja iz mape dohvaća set ključeva koji je moguće iskoristiti za dohvat svih elemenata iz mape
    
    ```java
    for(Integer key : mapaUnesenihBrojeva.keySet()) {
        System.out.println(key + " " + mapaUnesenihBrojeva.get(key));
    }
    ```

### Enumeracija
- tip podataka koji sadrži niz konstanti
- referentni tipovi s konstruktorima i varijablama
- pomoću metode *values* dohvaćaju se sve vrijednosti iz enumeracije
- vrijede sljedeća pravila
    - konstante u enumeracijama su implicitno označene modifikatorima final i static
    - nije moguće kreirati objekt koji predstavlja enumeraciju, već samo koristiti predefinirane vrijednosti
- primjer jednostavne enumeracije
    
    ```java
    public enum GodisnjeDoba {
        PROLJECE, LJETO, JESEN, ZIMA
    }
    ```
- primjer složene enumeracije

    ```java
        public enum StatusObrade {
            USPJESNA_OBRADA(1, "Uspješno obrađene sve transakcije"),
            TIMEOUT(2, "Neuspješna obrada, isteklo maksimalno vrijeme trajanja"),
            NEISPRAVNI_PODACI(3, "Neuspješna obrada, neispravni podaci u transakcijama");
         
            private Integer kod;
            private String opis;
         
            private StatusObrade(Integer kod, String opis) {
                this.kod = kod;
                this.opis = opis;
            }
         
            //getter metode za kod i opis
        }
    ```
- korištenje enumeracije

    ```java
    for(GodisnjeDoba godisnjeDoba : GodisnjeDoba.values()) {
        System.out.println(godisnjeDoba);
    }

    StatusObrade status = StatusObrade.USPJESNA_OBRADA;
    switch(status) {
        case USPJESNA_OBRADA:
            System.out.println("Poruka1");
            break;
        case TIMEOUT:
            System.out.println("Poruka2");
            break;
        case NEISPRAVNI_PODACI:
            System.out.println("Poruka3");
            break;
    }
    ```

### Stream
- dodani u Javi SE 8
- omogućuju paralelno obrađivanje podataka čime se poboljšavaju performanse
- svaka zbirka ili polje ima mogućnost pozivanje metode *stream* i nad dobivenim tokom obavljati operacije
- služe za obradu elemenata zbirke kroz nekoliko procesnih metoda -> stream pipeline
- sastoje se od međuoperacija (intermediate operation) i završnih operacija (terminal operation)
    - međuoperacije se pišu prije, ali se izvršavaju tek kad se obavi i završna operacija
- primjeri međuoperacija
    - filter
    - distinct
    - limit
    - map
    - sorted
- primjeri završnih operacija
    - forEach
        - obavlja procesiranje svakog elementa u toku
    - average
        - izračunava srednju vrijednost elemenata u toku koji sadrži numeričke vrijednosti
    - count
        - vraća broj elemenata u toku
    - max
        - vraća maksimalnu vrijednost iz toka koji sadrži numeričke vrijednosti
    - min
        - vraća minimalnu vrijednost iz toka koji sadrži numeričke vrijednosti
    - reduce
        - određuje jedinstvenu vrijednost toka korištenjem lambda funkcije
        - suma elemenata
    - collect
        -  kreira novu zbirku elemenata koja sadrži samo rezultate
    - toArray
        - kreira polje elemenata koje sadrži rezultate
    - findFirst
        - traži prvi element predstavljen objektom Optional
    - findAny
        - traži bilo koji element predstavljen objektom Optional
    - anyMatch
        - određuje ispunjava li bilo koji element uvjete definirane u međuoperacijama
    - allMatch
        - određuje ispunjavaju li svi elementi u toku uvjete definirane u međuoperacijama
    - iterate
    - takeWhile
    - dropWhile

```java
long count = allArtists.stream()
    .filter(artist -> artist.isFrom("London"))
    .count(); 
```
- na početkom pozivanjem metode *filter* se postavlja *kriterij* po kojem će se dohvaćati samo određeni elementi iz zbirke (ne kreira se nova *filtrirana* zbirka)
    - takve operacije se često kategoriziraju kao **lazy** (ne izvršavaju se odmah)
- metoda *count* na kraju pobrojava sve objekte iz zbirke koji su **prošli filtriranje** 
    - takve operacije se kategoriziraju kao *eager*, odmah se izvršavaju i okidaju izvođenje svih ostalih *lazy* operacija koje su prethodno izvršene
- **Pošto nema završne metode u ovom primjeru ne bi se ispisivalo ništa**

    ```java
    allArtists.stream()
        .filter(artist -> {
            System.out.println(artist.getName());
            return artist.isFrom("London");
        })
    ```
- ako metoda vraća Stream, onda je lazy
- ako metoda ne vraća Stram, onda je eager  


### IntStream
- specijalizirani tok za rad s cjelobrojnim vrijednostima

    ```java
    int[] numbers = {8, 10, 4, 9, 5, 7, 1, 3};

    System.out.print("Početne vrijednosti: ");
    IntStream.of(numbers).forEach(vrijednost -> System.out.print(vrijednost + " "));

    System.out.println();

    System.out.println("Broj elemenata: " + IntStream.of(numbers).count());
    System.out.println("Najveći element: " + IntStream.of(numbers).max().getAsInt());
    System.out.println("Suma elemenata: " + IntStream.of(numbers).reduce((x, y) -> x + y).getAsInt());

    System.out.print("Sortirani parni numbers: ");
    IntStream.of(numbers)
            .sorted()
            .filter(x -> x%2 == 0)
            .forEach(x -> System.out.print(x + " "));
    ```
    ```
    Početne vrijednosti: 8 10 4 9 5 7 1 3 
    Broj elemenata: 8
    Najveći element: 10
    Suma elemenata: 47
    Sortirani parni numbers: 4 8 10 
    ```


### Zbirke kroz verzije Jave
- Java 9
    - dodana mogućnost kreiranja *immutable* listi korištenjem *of* metode
        - Nakon kreiranja liste naknadne promjene njenog sadržaja rezultiraju bacanjem iznimke *UnsupportedOperationException*:
    
            ```java
            List<String> immutableList = List.of("Prvi","Drugi","Treći");

            immutableList.add("Četvrti"); // baca se iznimka

            // na sličan način moguće je kreirati i mapu
            Map<Integer, String> immutableMap = Map.of(1, "Prvi", 2, "Drugi", 3, "Treći");
            ```
    - novi način rada s streamovima

        ``` java
        IntStream.iterate(1, i -> i < 100, i -> i + 1).forEach(System.out::println);
        ```
    - dodana metoda *takeWhile* za ispis samo onih vrijednosti koje ispunjavaju zadani uvijet (i *dropWhile* koja kao rezultat ostavlja samo one vrijednosti koje ne ispunjavaju zadani uvijet)

        ```java
        Stream.of(1,2,3,4,5,6,7,8,9,10).takeWhile(i -> i < 5 ).forEach(System.out::println);
        Stream.of(1,2,3,4,5,6,7,8,9,10).dropWhile(i -> i < 5 ).forEach(System.out::println);
        ```

### Comparator
```java
public class PersonSalaryComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p2.getSalary().compareTo(p1.getSalary());
    }
}
```