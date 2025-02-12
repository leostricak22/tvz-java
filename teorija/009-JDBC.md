# JDBC - Baze podataka

# Uvod
- koristimo H2 bazu
- za kreiranje baze potrebno je odabrati postavku ***Generic H2 (Embedded)*** te upisati ime baze podataka, korisničko ime i lozinku
- za korištenje baze podataka potrebno je odabrati postavku ***Generic H2 (Server)***
- ***JDBC (Java Database Connectivity)*** omogućuje spajanje Java aplikacija na sve vrste baza podataka uz korištenje odgovarajućeg JDBC drivera
    - u obliku JAR datoteke
- tri programske aktivnosti
    - ostvarivanje veze s izvorom podataka
    - pripremanje i izvršavanje SQL upita
    - zatvaranje veze
- spajanje na bazu
    ```java
    private Connection connectToDatabase() throws IOException, SQLException {
        // dohvaćanje podataka za spajanje na bazu iz properties datoteke
        Properties props = new Properties();
        props.load(new FileReader("database.properties"));

        // DriverManager je klasa koja upravlja JDBC driverima
        return DriverManager.getConnection(props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }
    ```

# Pripremanje i izvršavanje upita
- korištenje klase ***Statement***
    ```java
    @Override
    public List<T> findAll() throws RepositoryAccessException {
        List<T> beverages = new ArrayList<>();

        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // Statement je interface, a connection.createStatement() vraća objekt koji ga implementira
            // Statement se koristi za izvršavanje SQL upita koji se ne moraju ponavljati
            Statement stmt = connection.createStatement();

            // ResultSet je interface, a stmt.executeQuery() vraća objekt koji ga implementira
            // executeQuery vraća ResultSet objekt koji sadrži sve retke koji zadovoljavaju upit
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BEVERAGE");

            // petlja se izvršava dok ima redaka u resultSet-u
            while (resultSet.next()) {
                Beverage beverage = extractBeverageFromResultSet(resultSet);
                // potrebno castanje jer je Beverage nadklasa
                beverages.add((T) beverage);
            }

            // kada se connection zatvori, automatski se zatvaraju i stmt i resultSet

            return beverages;
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        }
    }
    ```
- korištenje klase ***PreparedStatement***
    ```java
    @Override
    public T findById(Long id) {
        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // PreparedStatement se koristi za izvršavanje SQL upita koji se ponavljaju
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BEVERAGE WHERE ID = ?");
            // postavljanje parametara upita
            // počinje od 1, a ne od 0
            stmt.setLong(1, id);

            // executeQuery vraća ResultSet objekt koji sadrži sve retke koji zadovoljavaju upit
            ResultSet resultSet = stmt.executeQuery();

            // next() vraća true ako ima redaka u resultSet-u
            // ovdje se očekuje samo jedan redak
            if (resultSet.next()) {
                return (T) extractBeverageFromResultSet(resultSet);
            } else {
                throw new EmptyRepositoryResultException("No beverage with id " + id);
            }
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        }
    }
    ```
- spremanje podataka
    ```java
    @Override
    public void save(List<T> entities) {
        // connection se zatvara automatski jer je implementiran AutoCloseable
        try (Connection connection = connectToDatabase()) {
            // PreparedStatement se koristi za izvršavanje SQL upita koji se ponavljaju
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO BEVERAGE (NAME, PRICE, ALCOHOL_PERCENTAGE, ORIGIN) VALUES (?, ?, ?, ?)");

            // petlja se izvršava za svaki entitet
            for (T entity : entities) {
                // postavljanje parametara upita
                stmt.setString(1, entity.getName());
                stmt.setBigDecimal(2, entity.getPrice());
                stmt.setBigDecimal(3, entity.calculatePercentage());
                stmt.setString(4, entity.getOrigin().toString());

                // executeUpdate vraća broj redaka koji su promijenjeni
                stmt.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
            throw new RepositoryAccessException(ex);
        }
    }
    ```

# Transakcija prilikom izvršavanja upita
- transakcija predstavlja skup jedne ili više operacija koje se moraju izvršiti zajedno kao da se radi o jednoj transakciji
- svaka nova veza s bazom konfigurirana je tako da uvijek automatski sprema sve promjene u bazu podataka (auto-commit mode)
    - za promjenu tih postavki potrebno je staviti `connection.setAutoCommit(false);`
    ```java
    PreparedStatement updateStudenti1 = veza.prepareStatement(
        "UPDATE STUDENTI SET IME = ? WHERE JMBAG = ?");

    updateStudenti1.setString(1, "Petar");
    updateStudenti1.setString(2, "0024568238");
    updateStudenti1.executeUpdate();

    PreparedStatement updateStudenti2 = veza.prepareStatement(
        "UPDATE STUDENTI SET PREZIME = ? WHERE JMBAG = ?");

    updateStudenti2.setString(1, "Ivičić");
    updateStudenti2.setString(2, "0024568238");
    updateStudenti2.executeUpdate();

    veza.commit();
    veza.setAutoCommit(true);
    ```

# Properties datoteke
- u njima se nalaze ključni podaci koje nije najbolje držati unutar programskih koda
    - korisničko ime, lozinka,...
- pomoću njih se izbjegava hardkodiranje
- funkcioniraju po principu ***ključ-vrijednost***
- čitanje iz datoteke
    ```java
    Properties props = new Properties();
    props.load(new FileReader("database.properties"));
    ```
- primjer datoteke
    ```
    databaseUrl = neki.url.com
    username = user
    password = pass
    ```