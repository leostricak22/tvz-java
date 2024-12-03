# Java OOP
### Nasljeđivanje među klasama
- nadklasa (superclass)
- podklasa (subclass)
- Java podržava jednostruko nasljeđivanje korištenjem ključne riječi "extends"
	- svaka klasa može istovremeno nasljeđivati samo jednu nadklasu

### Modifikatori klasa
- public 
	- pristup iz svih klasa
- protected
	- pristup iz svih podklasa
- package 
	- pristup iz klasa u istom paketu
- private 
	- pristup iz klase


### Ključna riječ super
- služi za dohvat članova nadklasa
	- pristupanje: super.nesto
	- konstruktor: super()
		
	```java
	public class Automobil extends Vozilo {
		private String vrstaMotora;
		private int brojVrata;

		public Automobil(String vrstaMotora, int brojVrata, int brojPutnika, float potrosnja) {
			super(brojPutnika, potrosnja);
			this.vrstaMotora = vrstaMotora;
			this.brojVrata = brojVrata;
		}
	}
	```
- bez obzira navede li se ili ne, **prva naredba svakog konstruktora podklase je poziv konstruktora nadklase (super())**

### Nadjačavanje metoda u podklasama
- koristi se anotacija @Override
- notacije predstavljaju upute kompajleru	
- često se nadjačavaju equals, hashCode, toString, clone,...
	
	```java
	public class DvodimenzionalniOblik {
		private double visina;
		private double sirina;

		public double izracunajPovrsinu() {
			return visina * sirina;
		}
	}
	```
	```java
	public class Krug extends DvodimenzionalniOblik {
		private double polumjer;

		@Override
		public double izracunajPovrsinu() {
			return Math.PI * Math.pow(polumjer, 2);
		}
	}
	```

### Klasa Object
- sve klase u programskom jeziku Java izravno ili neizravno nasljeđuju klasu Object
- "Tata svih klasa"

### Polimorfizam
- omogucuje da se svi objekti čije klase imaju zajedničku nadklasu mogu spremiti u zajedničko polje, s time da se kod izvođenja programa mogu pozvati specifične metode za svaku od podklasa iz kojih su kreirani objekti
- objekt podklase može se tretirati kao objekt klase ako se nad njim izvrši "cast" operacija i u **tom slučaju može koristiti samo metode iz nadklase**

	```java
	Trokut trokut = new Trokut();
	((Oblik) trokut).metodaIzKlaseOblik();
	```
- da bi se provjerilo ako je objekt instanca neke klase, koristi se instanceof
	
	```java
	boolean isZaposlenik = osoba instanceof Zaposlenik;
	```


### Apstraktne klase i metode
- predviđene za nasljeđivanje
- ne može se kreirati objekt

	```java
	public abstract class Oblik {...}
	```
- ako klasa ima barem jednu apstraktnu metodu, ona mora biti apstraktna
- apstraktne metode **ne sadrže tijelo metode** 

	```java
	public abstract double izracunajPovrsinu();
	```

### Ključna riječ final
- spriječava daljnje nasljeđivanje neke klase

	```java
	public final class Krug extends DvodimenzionalniOblik {...}
	```
- spriječava daljnje nadjačavanje neke metode
	
	```java
	public final double izracunajPovrsinu();
	```
- označuju se i konstantne vrijednosti
	
	```java
	public static final String NAZIV_DRZAVE = "Hrvatska";
	```

### Builder Pattern
- u situacijama u kojima konstruktor neke klase prima puno parametara, a nisu sve vrijednosti dostupne, potrebno je koristiti "null" parametre
- implementacija Builder Patterna
	- bez Bulder Patterna (korištenje null-ova)

		```java
		public class BankAccount {
			private long accountNumber;
			private String owner;
			private String branch;
			private double balance;
			private double interestRate;

			public BankAccount(long accountNumber, String owner, String branch, double balance, double interestRate) {
				this.accountNumber = accountNumber;
				this.owner = owner;
				this.branch = branch;
				this.balance = balance;
				this.interestRate = interestRate;
			}

			// getteri i setteri
		}

		...
		BankAccount bankAccount = new BankAccount(456L, "Leo", "Springfield", 100.00, 2.5);
		BankAccount bankAccount2 = new BankAccount(456L, "Ena", null, 150.00, 3);
		```
	- sa Builder Patternom

		```java
		public class BankAccount {
			public static class Builder {
				private long accountNumber;
				private String owner;
				private String branch;
				private double balance;
				private double interestRate;

				public Builder(long accountNumber) { // identifikator
					this.accountNumber = accountNumber;
				}

				public Builder withOwner(String owner) {
					this.owner = owner;
					return this;
				}				

				// ... za sve parametre

				public BankAccount build() {
					BankAccount account = new BankAccount();
					account.accountNumber = this.accountNumber;
					account.owner = this.owner;
					account.branch = this.branch;
					account.balance = this.balance;
					account.interestRate = this.interestRate;

					return account;
				}
			}

			// konstruktor je sada privatan
			private BankAccount() {}

			...
			BankAccount account = new BankAccount.Builder(1234L)
						.withOwner("Leo")
						.atBranch("SpringField")
						.openingBalance(100)
						.atRate(2.5)
						.build();

			BankAccount account = new BankAccount.Builder(4567L)
						.withOwner("Leo")
						.atBranch("SpringField")
						.openingBalance(100)
						.atRate(2.5)
						.build();
		}
		```

### Sučelja
- omogućuju da klase koje se međusobno ne nasljeđuju implementiraju zajedničku skupinu metoda
- sučelje opisuju koje sve "operacije" se trebaju moći obavljati
	- ne sadrže nužno i njihovu implementaciju

	```java
	public interface Elektricno {
		void ukljuci();
	}
	```
- implementiranje sučelja -> ključna riječ implements

	```java
	public class ElektricnaGitara extends Gitara implements Elektricno {...}
	```
- u slučaju da klasa ne implementira sve metode iz sučelja, mora biti apstraktna
- sučelje može biti i tip objekata


### Proširenja funckionalnosti sučelja kroz verije Jave
- Java 8
	- sučelja mogu sadržavati i **default** implementacije metoda
		- klasa koja implementira sučelje s default metodom može koristiti tu podrazumijevanu implementaciju ili je nadjačati i defenirati vlastitu implementaciju
		
		```java
		public interface PracenjeVremena {
			default public String dohvatiDatumVrijeme() {
				LocalDateTime localDateTime = LocalDateTime.now();
				return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
			}
		}
		```
	- omogućeno korištenje **statičkih metoda** unutar sučelja
		- statičke metode sadrže zajedničku logiku za sve objekte klase koja implementira takvo sučelje i mogu se koristiti izravno iz same klase (bez kreiranja objekta)
		
		```java
		public interface Provjera {
			public static boolean provjeriPrazanString(String tekst) {
				if (tekst == null || tekst.isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		}
		```
	- korištenje lambda izraza
		- svako sučelje koje koristi **samo jednu apstraktnu metodu** naziva se **funkcionalnim sučeljem** 
			- Comparator
			- ActionListener
			- Runnable
			- ...
		- potrebno je napisati što je potrebno napraviti, a ne kako
		- lambda izrazi omogućuju pisanje skraćene notacije za kreiranje anonimnih metoda koje kompajler automatski prevodi u anonimne klase
			- anonimne klase su one klase čije tijelo se definira prilikom instanciranja objekta te klase

			```java
			Comparator<String> c = new Comparator<String>() {
				int compare(String s, String s2) {...} 
			};
			```			
			- sintaksa lambda funkcija se sastoji od definiranja liste parametara, nakon čega slijedi oznaka za strelicu "->" i na kraju tijelo lambda funkcije:
				(lista parametara) -> {naredbeLambdaFunkcije}

				```java
				(int x, int y) -> {return x+y;}
				```
- Java 9
	- unutar sučelja omogućeno je kreiranje i privatnih metoda

		```java
		
		public interface Sucelje {
			private void test() {
				System.out.println("Test");
			}

			public void test2() {
				test();
			}
		}
		```
- Java 14
	- zapisi (records) omogućavaju definiranje tipova podataka koji su nepromijenjivi i namjena im je čuvanje podataka
	
		```java
		public record Person(String name, String address) {}
		```
	- kreiranje i dohvaćanje recorda

		```java
		Person person = new Person("Leo", "Nigdjezemska");

		person.name();
		person.address();
		```
- Java 15
	- pojednostavljuje način poziva operatora instanceof
	- do Jave 15
		
		```java
		if(animal instanceof Cat) {
			Cat cat = (Cat)animal;
			return cat.meow();
		}
		```
	- od Jave 15

		```java
		if(animal instanceof Cat cat) {
			return cat.meow();
		}
		```

- Java 17
	- zapepačena (sealed) sučelja
		- omogućuju definiranje koje klase se mogu implementirati ili nasljeđivati
		- koriste se modifikatori sealed i non-sealed

			```java
			public sealed interface Service permits Car, Truck { // klase Car i Truck trebaju biti sealed, non-sealed ili final
				int getMaxServiceIntervalInMonths();
				default int getMaxDistanceBetweenServicesInKilometers() {
					return 100000;
				}
			}
			```
			- moguće je zapečatiti i klase