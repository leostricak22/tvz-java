Tipovi podataka:
	String
	BigDecimal
	Long
	Integer
	kada se ne radi s primitivnim tipovima iz C-a, treba se koristiti compareTo() umjesto <,>,=
		> 0 -> veće
		= 0 -> jednako
		< 0 -> manje


Scanner
	Scanner sc = new Scanner(System.in);
	...
	sc.nextLine(); // unos stringa
	sc.nextInt(); // unos integera
		obavezno: sc.nextLine(); // Nakon unosa broja ostaje enter u bufferu i treba se očistiti
	sc.nextBigDecimal();
	...
	sc.close();
	
Exception
	InputMismatchException
	NullPointerException

Random
	Random random = new Random();
	random.nextInt(5); -> 0, 1, 2, 3, 4

DODATNO
	ako se traži long -> i+1 se treba napisati kao (long) (i+1) ili Long.valueOf(i+1)
	
OBJECT ORIENTED PROGRAMMING
	abstract class -> ne mogu se kreirati objekti iz te klase (služi nasljeđivanju)
	
Comparator
	
Arrays
	sort

Lambda izrazi
	Arrays.sort(prezimena, (p1, p2) -> p1.compareTo(p2));

Operatori
	instanceof
		boolean isZaposlenik = osoba instanceof Zaposlenik;
	
Builder Pattern

java.lang.Object
- koje sve metode ima ta klasa

Pattern

Matcher

compareTo -> Comparable