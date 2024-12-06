# Java exceptions

### Uvod
- predstavljaju problem koji nastaje kod izvođenja programa
- u Javi su predstavljene klasama koje izravno ili neizravno nasljeđuju klasu java.lang.Throwable
- moguće je hvatati (catch) i bacati (throw) iznimke
- često se zapisuju u *log* datoteke

### try catch blokovi
- obrada iznimaka obavlja se korištenjem blokova *try* i *catch*
- blok *try* se izvodi tako dugo dok ne dođe do iznimke

    ```java
    boolean nastaviPetlju = false;
    do {
        try {
            System.out.print("Unesite dva broja: ");

            int prvi = unos.nextInt();
            int drugi = unos.nextInt();
            int rezultat = podijeli(prvi, drugi);

            System.out.println("Rezultat dijeljenja je: " + rezultat);
            nastaviPetlju = false;
        } catch (InputMismatchException ex1) {
            System.out.println("Morate unijeti bročane vrijednosti.");
            unos.nextLine();
            nastaviPetlju = true;
        } catch (ArithmeticException ex2) {
            System.out.println("Unesite ispravne vrijednosti za dijeljenje.");
            unos.nextLine();
            nastaviPetlju = true;
        }
    } while (nastaviPetlju);
    ```

### Multi-catch blok
- uvedeno u Javi 7
- može sadržavati bilo koji broj iznimaka

    ```java
    catch (PrviTipIznimke | DrugiTipIznimke | TreciTipIznimke ex) {...}
    ```

### Finally blok
- postavlja se nakon *catch* bloka i **uvijek** se izvodi, bez obzira ako je došlo do iznimke ili ne

    ```java
    try {...}
    catch {...} // neobavezan
    finally {...}
    ```

### Hijerarhija iznimaka
- sve klase koje predstavljaju iznimke u Javi izravno ili neizravno nasljeđuju klasu **Exception** koja ima nadklasu **Throwable**
- Exception 
    - Error (Ne hvata se)
        - AWTError, ThreadDeath, VirtualMachineError
    - Exception (Predstavljaju iznimke koje se događaju tijekom izvođenja programa)
        - IOException
        - RuntimeException

### Označene i neoznačene iznimke
- postoje označene (checked) i neoznačene (unchecked) iznimke
- kompajler postavlja posebne zahtjeve za obrađivanje označenih iznimaka
- vrsta iznimke određuje se klasom koju ta iznimka nasljeđuje
    - Exception ili RuntimeException
- RuntimeException i Error iznimke spadaju u skupinu neoznačenih iznimaka
    - najvažnija karakteristika neoznačenih iznimaka je ta da se takve iznimke **ne moraju obrađivati**
    - kompajler neće baciti grešku ako se ne navede *catch* blok
- Exception iznimke nazivaju se označenim iznimkama
    - označavaju uvjete kod izvođenja programa koje nisu pod izravnom kontrolom programa
    - pr. kad ne postoji datoteka koju program otvara

### Bacanje iznimaka
- iznimke se mogu baciti ključnom riječi *throw*
- neoznačene iznimke

    ```java
    throw new RuntimeException("Pogreška u programu!");
    ```
- označene iznimke

    ```java
    throw new IOException("Pogreška u programu!");
    ```
- ako se iznimka ne obrađuje na mjestu gdje se može dogoditi, moguće je kod metode navesti *throws*
    - **ne treba** se navesti kod unchecked iznimaka
    
    ```java
    public void provjera() throws IOException {...}
    ```
- ako metoda baca iznimku korištenjem *throw*, nema smisla da se ta iznimka odmah i obrađuje, već je potrebno koristiti ključnu riječ *throws*
    
    ```java
    // Ovaj kod nema smisla, "bacili ste si kamen u glavu"
    try {
        throw new IOException("Pogreška u radu s datotekom!");
    } catch (IOException ex) {...}
    ```

### Kreiranje vlastitih iznimaka
- u praksi je često potrebno kreirati nove klase koje predstavljaju iznimke
- vrstu iznimke (označena ili neoznačena) moguće je postaviti odabirom nadklase Exception ili RuntimeException
- osim samog tipa iznimke preporuča se pisanje vlastitih konstruktora koji omogućavaju kreiranje objekta iznimke

    ```java
    public class MyException extends Exception {
        public MyException() {
            super("Dogodila se pogreška u radu programa!");
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(Throwable cause) {
            super(cause);
        }
    }
    ```

### Stack trace
- ispis stoga iznimke
- sadržava ključne informacije o iznimci
- moguće je pozvati metodu *printStackTrace* za ispis iznimke
- moguće je i dohvatiti poruku korištenjem metode *getMessage*

### Iznimke
- ArithmeticException
    - dijeljenje s nulom
    - RuntimeException
- InputMismatchException
    - pogrešan unos tekstualnih vrijednosti umjesto brojčanih
    - RuntimeException
- NullPointerException
    - null
    - RuntimeException
- ClassCastException
    - RuntimeException
- FileNotFoundException
    - Exception
- UnsupportedOperationException
- EmptyStackException