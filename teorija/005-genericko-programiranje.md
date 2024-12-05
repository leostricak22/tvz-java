# Java generičko programiranje

### Uvod
- uvedeno u Javi 5
- zašto postoji potreba za generičkim programiranjem?
    - ako je potrebno napisati metodu koja će ispisivati cjelobrojne članove jednodimenzionalnog polja, to je moguće napraviti na sljedeći način

        ```java
        public static void printArray(Integer[] inputArray) {
            for (Integer element : inputArray) {
                System.out.printf("%s ", element);
            }
            System.out.println();
        }

        public static void main(String[] args) {
            Integer[] integerArray = {1, 2, 3, 4, 5, 6};
            System.out.printf("Array integerArray sadrži: ");
            printArray(integerArray);
        }
        ```
    - u slučaju da je potrebno ispisati elemente polja koje sadrži npr. *Double* ili *Character* tipove podataka, potrebno je napisati još jednu overloadanu metodu za ispis koja će se razlikovati samo u tipu elemenata koje ispisuje

        ```java
        public static void printArray(Double[] inputArray) {
            for (Double element : inputArray) {
                System.out.printf("%s ", element);
            }
            System.out.println();
        }
        public static void printArray(Character[] inputArray) {
            for (Character element : inputArray) {
                System.out.printf("%s ", element);
            }
            System.out.println();
        }
        ```
    - metode se razlikuju samo po tipu
        - ta činjenica može se iskoristiti za pisanje samo jedne metode koja koristi generički parametar **T**
        - **T** predstavlja sve referentne tipove u Javi
- primjer generičke metode

    ```java
    public static <T> void printArray(T[] inputArray) {
    for (T element : inputArray)
        System.out.printf("%s ", element);

    System.out.println();
    }
    ```
- svaka generička metoda ima definiciju parametra **\<T\>** koji označava da će se unutar same metode koristiti generički tip s oznakom **T**
- pozivi su identični poziva metoda bez generičkih parametara

### Prevođenje generičkih metoda
- sva pojavljivanja parametra *T* zamjenjuje se sa stvarnim tipom podatka
- po defaultu se *T* mijenja **Object** tipom
- ako je potrebno ograničiti da metoda prima samo objekt koji nasljeđuje klasu *Student*:

    ```java
    public static <T extends Student> void usporedi(T vrijednost1, T vrijednost2) {...}
    ```
- ako se implementira sučelje treba **DODATI** ključnu riječ **EXTENDS**, a ne *implements*
    
    ```java
    // T mora primati sučelje koja sama sebe može uspoređivati
    public static <T extends Comparable<T>> T maximum(T x, T y, T z) {
        T max = x;

        if (y.compareTo(max) > 0)
            max = y;

        if(z.compareTo(max) > 0)
            max = z;

        return max;
    }

    public static void main(String[] args) {
        System.out.println(maximum(3,4,5));
        System.out.println(maximum(3.2,3.1,2.3));
        System.out.println(maximum("apple", "banana", "pear"));
    }
    ```

### Generičke klase
- nazivaju se i parametrizirane klase ili tipovi
- omogućavaju definiranje tipova objekata prilikom instanciranja
- primjer generičke klase koja predstavlja memorijsku strukturu stoga
    
    ```java
    public class Stack<T> {
        private List<T> elements;

        ...
    }
    ```
- kod dohvaćanja podataka iz klase potrebno je koristiti *cast* operaciju

    ```java
    rawStack.push(123);
    Integer broj = (Integer) rawStack.pop();
    ```
- nedefinirani tipovi 
    - engl. Raw Types
    - u slučaju da se koristi generička klasa i kod inicijaliziranja se ne navede tip podataka, tada se koristi ***Object***

        ```java
        Stack doubleStack = new Stack(5);
        ```
    - mogućnost je ostavljena zbog unazadne kompatibilnosti (zbog prošlih verzija Jave)
    - preporuča se izbjegavanje korištenja nedefiniranih tipova jer se u tom slučaju mogu koristiti svi mogući tipovi podataka i povećava se vjerojatnost generiranja iznimke *ClassCastException*
- zamjenski simboli
    - engl. Wildcards
    - ako je potrebno napisati funkciju koja izračunava sumu numeričkih vrijednosti spremljenih u zbirku kao što je lista, također je moguće koristiti generičku metodu
    - jedina stvar koju u tom slučaju treba ograničiti je da zbirka sadržava samo numeričke vrijednosti
        - objekte koji izravno ili neizravno nasljeđuju klasu *Number*

        ```java
        public static double sum(List<? extends Number> list ) {
            double total = 0;
            for(Number element : list) {
                total += element.doubleValue();
            }

            return total;
        }
        ```
    - zamjenski simbol **?** označava *nepoznati tip* koji mora nasljeđivati klasu *Number*
    - mana korištenja *?* je ta da nije moguće znati o kojem se tipu radi