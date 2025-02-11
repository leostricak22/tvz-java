# Java datoteke

# Uvod
- dvije vrste
  - teksutalne
  - binarne
- svaka datoteka je ***niz bajtova***
- na kraju datoteke je oznaka ***end-of-file***
- u datoteke je moguće spremati cijele objekte ***serijalizacijom***, a čitati ***deserijalizacijom***

# Vrste tokova podatka
- binarni format
  - char ima 2 bajta, int 4 bajta, double 8 bajtova,...
  - koristi se kod binarnih datoteka
- znakovni format
  - svaki znak ima 2 bajta
  - koristi se kod teksutalnih datoteka
- prilikom otvaranja datoteka potrebno je kreirati pripadajući objekt čiji konstruktor komunicira s operacijskim sustavom
- u Javi postoje 3 različita toka podataka
  - System.in
  - System.out
  - System.err

# Klasa java.io.File
- predstavnja element datotečnog sustava (može biti datoteka ili mapa)
- sadrži putanju, ime i veličinu datoteke, ali ne i sam sadržaj
- u slučaju nedostatka prava baca se iznimka SecurityException
- može kreirati datoteku na sljedeće načine
  ```java
  File wf1 = new File("moj.htm"); // trenutni radi direktorij
  File wf2 = new File("java\\html\\IO.html"); // relativna putanja
  File wf3 = new File("D:\\java\\25.txt"); // apsolutna putanja
  ```
- sadrži niz metoda
  - exists
  - isFile
  - isDirectory
  - getName
  - getPath

# Sučelja i klase iz paketa java.nio.file
- sučelje Path
  - objekti klasa koji implementiraju to sučelje predstavljaju lokaciju datoteka ili mape
  - ne omogućavaju čitanje sadržaja iz datoteke
- klasa Paths
  - sadrži statičke metode za dohvat ***Path*** objekata koji predstavljaju datoteku ili mapu
- klasa Files
  - sadrži statičke metode za rad s datotekama ili mapama
  - kopiranje datoteka
  - kreiranje ili brisanje mapa
  - dohvaćanje informacija o datotekama
  - čitanje i promjena sadržaja datoteka
- sučelje DirectoryStream
  - objekti klasa koji implementiraju to sučelje mogu koristiti sadržaj datoteke

# Klasa Formatter
- služi za oblikovanje teksta koji se može zapisivati u datoteku
  ```java
  public static void main(String[] args) {
      try {
          Formatter output = new Formatter("files/output.txt");

          // funkcionira na principu System.out.printf
          output.format("Hello, %s!", "world");

          // ako se ne zatvori, neće se ništa zapisati u datoteku
          output.close();
      } catch (FileNotFoundException e) {
          // ako ne postoji datoteka, baca se iznimka FileNotFoundException
          log.error("Datoteka nije pronađena!", e);
      }
  }
  ```

# Tokovi za čitanje i zapisivanje podataka u datoteke
- u Javi postoji nekoliko klasa koje predstavljaju tokove za razmjenu podataka s datotekama
- dijele se na
  - ulazni tokovi podataka
  - izlazni tokovi podataka
- osnovne apstraktne klase iz kojih su izvedene ostale klase za čitajne i pisanje toka bajtova za ***binarne*** datoteke
  - ***java.io.InputStream*** (šalje bajtove iz vanjskog izvora u Java program)
    - ObjectInputStream
    - FileInputStream
    - PipedInputStream
    - FilterInputStream
      - DataInputStream
      - BufferedInputStream
      - PushbackInputStream
  - ***java.io.OutputStream*** (šalje bajtove iz Java programa u neko vanjsko odredište)
    - ObjectOutputStream
    - FileOutputStream
    - PipedOutputStream
    - FilterOutputStream
      - DataOutputStream
      - BufferedOutputStream
- osnovne apstraktne klase (i njihove podklase) iz kojih su izvedene ostale klase za čitanje i zapisivanje znakovnih tokova su:
  - ***java.io.Reader*** (šalje znakove iz vanjskog izvora u Java program)
    - BufferedReader
    - FilterReader
    - PipedReader
    - InputStreamReader
      - FileReader
  - ***java.io.Writer*** (šalje znakove iz Java programa u neko vanjsko odredište)
    - BufferedWriter
    - FilterWriter
    - PipedWriter
    - PrintWriter
    - OuputStreamWriter
      - FileWriter

# Čitanje i zapisivanje podataka u binarne datoteke
- ***Ovo ne koristimo. Bolje je čitati odma cijele objekte, a ne bajt po bajt***
- osnovna metoda klase InputStream je
  ```java
  public abstract int read() throws IOException
  ```
- čita 1 bajt iz ulaznog toka, a vraća cjelobrojnu vrijednost tog bajta
- metoda read ***blokira*** izvođenje ostatka programa (čeka tako dugo dok se jedan bajt ne pročita)
- osnovna metoda klase OutputStream je
  ```java
  public abstract void write(int b) throws IOException
  ```
- šalje jedan bajt podataka preko izlaznog toka do odredišta koje taj znak intepretira na određeni način

# Blok "try-with-resources"
- svaku datoteku je nakon njenog korištenja potrebno zatvoriti pozivom metode "close" nad objektom koji predstavlja tok podataka
- za automatiziranje tog procesa, od Jave 7 uveden je poseban "try" blok koji automatski poziva metodu "close"
  ```java
  private static final String FILENAME = "files/input.txt";

  public static void main(String[] args) {
      // try-with-resources blok
      // objekt treba implementirati AutoCloseable ili Closeable sučelje, Closeable nasljeđuje AutoCloseable
      // nakon try bloka, objekt će biti zatvoren
      try (FileInputStream fis = new FileInputStream(FILENAME)) {
          // kraj datoteke je označen s -1
          for (int n=fis.read(); n != -1; n = fis.read()) {
              System.out.print((char) n);
              // ispisuje se bajt po bajt
              // bez castanja u char, ispisivali bi se ascii vrijednosti
          }

          System.out.flush();
          // flush metoda odmah ispisuje sve što je u bufferu
          // bez flush metode, ispis bi bio odgođen dok se buffer ne napuni ili zatvori
      } catch (IOException e) {
          System.err.println("Došlo je do greške prilikom čitanja datoteke!");
      }
  }
  ```
- može se inicijalizirati više objekata, odvojenih sa ";" (točka-zarez)
- od Jave 9 je moguće koristiti "try-with-resources" na način da se objekti mogu definirati i prije bloka, pri čemu trebaju biti final ili effectively final

# Korištenje streamova u radu s datotekama
- klasa ***Files*** sadrži i nekoliko metoda koje su vezane uz streamove i drastično olakšavaju rad s datotekama
  - lines(Path putanja)
    - dohvaća sve linije unutar datoteke na zadanoj putanji
  - list(Path putanja)
    - dohvaća sve datoteke na zadanoj putajni
  - newDirectoryStream(Path putanja)
    - otvara mapu i omogućava dohvaćanje njenog sadržaja
  - walk(Path putanja)
    - otvara mape i sve podmape u hijerarhiji te omogućava dohvaćanje sadržaja tih mapa
  - readString
    - čita cijeli sadržaj datoteke u jedan String s opcijeom definiranja "CharacterSeta"
- nakon dohvaćanja podataka, moguće koristiti razne metoda za manipulaciju tih podataka temeljene na lambda izrazima
  - filter
  - foreach
  - limit
  - map
  ```java
  public static void main(String[] args) {
    List<String> listaStringova = new ArrayList<>();

    try (Stream<String> stream = Files.lines(new File(FILENAME).toPath())) {
        listaStringova = stream.collect(Collectors.toList());
        // ili skraćeno -> stream.toList();
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom čitanja datoteke!");
    }

    listaStringova.forEach(System.out::println);
  }
  ```
- čitanje datoteka iz direktorija pomoću streamova
  ```java
  public static void main(String[] args) {
    // čitanje direktorija, čitaju se samo datoteke u trenutnom direktoriju, a ne i poddirektoriji
    try (Stream<Path> stream = Files.list(new File(".").toPath())) {
        stream.filter(p -> !p.getFileName() // dohvaćanje imena datoteke
                        .toString() // pretvaranje u string
                        .startsWith(".")) // filtriranje datoteka da ne počinju s točkom
                .limit(3) // ograničenje na 3 datoteke
                .forEach(System.out::println); // ispis datoteka
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom čitanja direktorija!");
    }

    // kreira stream svih datoteka i njihovih poddatoteka u trenutnom direktoriju
    // Files.walk metoda je rekurzivna
    try (Stream<Path> stream = Files.walk(Path.of("."))) {
        stream.filter(p -> !p.getFileName()
                        .toString()
                        .startsWith(".")) // filtriranje datoteka da ne počinju s točkom
                .forEach(System.out::println); // ispis datoteka
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom čitanja direktorija!");
    }
  }
  ```

# Serijalizacija i deserijalizacija
- ako je potrebno zapisati objekt u datoteku, potrebno ga je ***serijalizirati***
- serijalizacija je pretvaranje objekta u binarni oblik
  - obrnuti proces je ***deserijalizacija***
- klase čiji se objekti žele serijalizirati moraju implementirati sučelje ***Serializable*** (te klase koje klasa nasljeđuje) 
  - sučelje ***Serializable*** nema ni jednu metodu
  ```java
  private static void serializeBeverages(List<Beverage> beverages) {
    // za serijaliziranje objekata u datoteku koristi se klasa ObjectOutputStream i metoda writeObject
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("files/beverages.dat"))) {
        // ObjectOutputStream baca iznimku IOException, a FileOutputStream baca iznimku FileNotFoundException

        // serijalizacija liste objekata
        out.writeObject(beverages);
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom pisanja u datoteku!");
    }
  }

  private static List<Beverage> deserializeBeverages() {
    // za deserijaliziranje objekata iz datoteke koristi se klasa ObjectInputStream i metoda readObject
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("files/beverages.dat"))) {
        // ObjectInputStream baca iznimku IOException, a FileInputStream baca iznimku FileNotFoundException
        List<Beverage> beverages = (List<Beverage>) in.readObject();
        // in.readObject() baca iznimku ClassNotFoundException

        // vraćanje deserijalizirane liste objekata
        return beverages;
    } catch (IOException | ClassNotFoundException e) {
        System.err.println("Došlo je do greške prilikom čitanja iz datoteke!");
    }

    return new ArrayList<>();
  }

  public static void main(String[] args) {
    // serijalizacija objekata
    serializeBeverages(List.of(
            new Beverage(1L, "Vino", BigDecimal.valueOf(20.0), BigDecimal.valueOf(12.0)),
            new Beverage(2L, "Pivo", BigDecimal.valueOf(10.0), BigDecimal.valueOf(5.0)),
            new Beverage(3L, "Rakija", BigDecimal.valueOf(15.0), BigDecimal.valueOf(40.0))
    ));

    // deserijalizacija objekata
    deserializeBeverages().forEach(beverage ->
            System.out.println(
                beverage.getId() + " " +
                beverage.getName() + " " +
                beverage.getPrice() + " " +
                beverage.calculatePercentage()));
  }
  ```

# Primjer rada s datotekama i mapama
  ```java
  public static void main(String[] args) {
      // radi i Path.of(FILENAME)
      Path path = Paths.get(FILENAME);

      // exists metoda provjerava postoji li datoteka
      if (!Files.exists(path)) {
          System.err.println("Datoteka ne postoji!");
      }

      System.out.printf("%s postoji!%n", path.getFileName());
      System.out.printf("%s mapa%n", Files.isDirectory(path) ? "je" : "nije");
      System.out.printf("%s apsoultna putanja%n", path.isAbsolute() ? "je" : "nije");

      try {
          System.out.printf("Posljednja izmjena: %s%n", Files.getLastModifiedTime(path));
          System.out.printf("Veličina datoteke: %s%n", Files.size(path));
      } catch (IOException e) {
          System.err.println("Došlo je do greške prilikom čitanja!");
      }

      // ispisuje apsolutnu putanju do datoteke
      // ako datoteka ne postoji, ispisuje se putanja do trenutne mape + ime datoteke koja ne postoji
      System.out.printf("Putanja do datoteke: %s%n", path.toAbsolutePath());

      if(Files.isDirectory(path)) {
          System.out.println("Sadržaj mape:");

          try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
              directoryStream.forEach(entry -> System.out.println(entry.getFileName()));
          } catch (IOException e) {
              throw new RuntimeException(e);
          }
      }
      
      try {
          Files.copy(path, Paths.get("files/input2.txt"));
      } catch (IOException e) {
          System.err.println("Došlo je do greške prilikom kopiranja datoteke!");
      }
  }
  ```

# Čitanje tekstualne datoteke
- BufferedReader liniju po liniju
  ```java
  public static void main(String[] args) {
      try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
          String line;
          while ((line = reader.readLine()) != null) {
              System.out.println(line);
          }
      } catch (IOException e) {
          System.err.println("Došlo je do greške prilikom čitanja datoteke!");
      }
  }
  ```
- pomoću Files klase u jedan String (Java 11)
  ```java
  public static void main(String[] args) {
      try {
          String text = Files.readString(Path.of(FILENAME));
          System.out.printf("Pročitana datoteka:%n%s", text);
      } catch (IOException e) {
          System.err.println("Došlo je do greške prilikom čitanja datoteke!");
      }
  }
  ```

# Pisanje u teksialne datoteke
- spremanje liniju po liniju pomoću PrintWritera
  ```java
  public static void main(String[] args) {
    // PrintWriter baca FileNotFoundException i SecurityException, a FileWriter IOException
    // ako datoteka ne postoji, stvara se nova
    // ako postoji datoteka s istim imenom, njena sadržaj će biti izbrisan
    try (PrintWriter out = new PrintWriter(new FileWriter(FILENAME))) {
        int i=0;
        do {
            // ispisuje u datoteku
            out.println("Hello, world! " + i);
            i++;
        } while (i < 10);
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom pisanja u datoteku!");
    }
  }
  ```
- spremanje cijelog Stringa pomoću klase Files
  ```java
  public static void main(String[] args) {
    String text = "Ovo je tekst koji se zapisuje u datoteku!\nDrugi redak teksta.";

    Path datoteka = Path.of(FILENAME);

    // ako datoteka ne postoji, stvara se nova
    // ako postoji datoteka s istim imenom, njena sadržaj će biti izbrisan
    try {
        Files.writeString(datoteka, text);
    } catch (IOException e) {
        System.err.println("Došlo je do greške prilikom pisanja u datoteku!");
    }
  }
  ```

# Na koje sve načine dobiti file path?
  ```java
  public static void main(String[] args) {
    System.out.println(Path.of(FILENAME));
    System.out.println(Paths.get(FILENAME));
    System.out.println(new File(FILENAME).toPath());
  }
  ```