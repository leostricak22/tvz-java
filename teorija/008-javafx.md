# JavaFX

# Uvod u JavaFX
- svaka klasa koja predstavlja polaznu točku JavaFX aplikacije mora nasljeđivati klasu ***javafx.application.Application***
  - omogućuje funkcionalnosti životnog ciklusa aplikacije kao što su inicijalizacija, pokretanje ili zaustavljanje izvođenja aplikacije
- omogućuje upravljanje aplikacijom odvojeno od glavne niti
- sadrži metodu ***launch*** koja poziva nadjačanu metodu ***start***
- metoda ***start*** prima objekt klase ***javafx.stage.Stage*** koji omogućava pokretanje zasebne niti koja će se izvodini na grafičkom sučelju aplikacije
- objekt klase Scene potrebno je dodati u objekt klase ***Stage*** koji omogućava prikaz grafičkog sučelja
- koncept JavaFX Scene Graph omogućuje korištenje više različitih ***Scene*** objekata na jednom ***Stage*** objektu
- objekti klase ***Scene***
  - omogućuju postavljanje veličine ekrana, naslova, layout,...
  - omogućeno je i definiranje lokacije i naziva CSS datoteke
  ```java
  // dodavanje css datoteke
  scene.getStyleSheets().add(getClass().getResource("style.css").toExternalForm());
  
  // implementiranje stilova
  root.setStyle("-fx-background: rgb(225, 228, 203)");
  primaryStage.setScene(scene);
  ```
- kreiranje prve scene i main stage-a
  ```java
  private static Stage mainStage;

  @Override
  public void start(Stage stage) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(new URL(
              "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/welcomePageScreen.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

      mainStage = stage;

      stage.setTitle("Pretraga pića");
      stage.setScene(scene);
      stage.show();
  }

  public static void main(String[] args) {
      launch();
  }

  public static Stage getMainStage() {
      return mainStage;
  }
  ```
- kasniji poziv nove scene:
  ```java
  public void showSearchBeveragesScreen() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(new URL(
            "file:./src/main/resources/hr/javafx/coffe/caffee/javafxcaffee/searchBeveragesScreen.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1065, 611);

    CoffeeShopApplication.getMainStage().setTitle("Pretraga pića");
    CoffeeShopApplication.getMainStage().setScene(scene);
    CoffeeShopApplication.getMainStage().show();
  }
  ```
- poziv funkcije nakon što je prozor prikazan
  ```java
  primaryStage.setOnShown((WindowEvent we) -> {
    // Kod koji će se izvršiti kada se prikaže prozor
    System.out.println("Prozor je prikazan");
  });
  ```

# FXML
- pomoću Scene Buildera (Gluon) omogućeno dizajniranje grafičkog sučelja
- posebno oblikovana XML datoteka FXML -> FX Markup Language
- omogućuje odvajanje prezentacijske logike od programske logike u JavaFX aplikacijama (backend i frontend)
- uključivanje FXML-a
  ```java
  FXMLLoader fxmlLoader = new FXMLLoader(new URL(_URL_));
  Scene scene = new Scene(fxmlLoader.load());

  // ili dodati scenu na komponentu
  BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Sample.fxml"));
  ```

# Komponente
- Label
- TextField
- TableColumn
- TableView
- ComboBox
- DatePicker
- MenuBar
- Menu
- MenuItem
- Button
- ToolBar
- ToggleButton
  - kad ga pritisnemo, ostaje pritisnut cijelo vrijeme
- ToolTip
- Circle
- Rectangle
- Tab
  - može se dodati samo u TabPane
- TableView
- TableColumn
- Accordion
- TreeView
- TreeItem
- TreeTableView
- TreeTableColumn
- SplitMenuButton
- RadioButton
  - dodati u Toggle group, da su u istj grupi
- HyperLink
- CheckBox
- TextArea
- PasswordField
- ProgressBar
- ProgressIndicator
- Slider
- ScrollBar

# Organizatori rasporeda komponenti
- javafx.scene.layout. ...
- GridPane
  - omogućuje umetanje elemenata na grafičko sučelje korištenjem "tablice" koja ima određen broj redaka i stupaca
  - kod dodavanja elemenata potrebno je dodijeliti "koordinate" ćelije tablice
  - ColumnConstraints, RowConstraints
    - možemo odrediti koliko su visoki, široki,...
  - kod dodavanja u ćeliju, zamijenjen redak i stupac 
  ```java 
  GridPane gridPane = new GridPane();

  Label fNameLbl = new Label("First name");

  GridPane.setHalignment(fNameLbl, HPos.RIGHT);
  gridPane.add(fNameLabel, STUPAC, REDAK);
  ```
- VBox
  - postavlja elemente u vertikalan redak, jedan ispod drugog
  - definiranje razmaka između elemenata
- HBox
  - postavlja elemente u horizontalan redak, jedan iza drugog
  - definiranje razmaka između elemenata
- BorderPane
  - omogućuje postavljanje komponenti u pet različitih područja
    - TOP
    - LEFT
    - CENTER
    - RIGHT
    - BOTTOM
- FlowPane
  - omogućuje postavljanje komponenti jedne iza druge do kraja raspoloživog retka nakon čega nastavlja u sljedećem retku
- StackPane
- Pane
- AnchorPane
- TabPane
- TitledPane
- SplitPane
- ScrollPane
- ContextMenu
  - desni klik

# Dijalog
- "Alert"
  ```java
  Alert alert = new Alert(AlertType.INFORMATION);
  alert.setTitle("Information Dialog");
  alert.setHeaderText("Look, an Information Dialog");
  alert.setContentText("I have a great message for you!");
  alert.showAndWait();  // kad se pritisne gumb se gasi
  ```

# MVC arhitektura
- Model View Controller
- JavaFX se temelji na MVC arhitekturi
  - model       -> podaci
  - view        -> grafičko sučelje
  - controller  -> logika aplikacije
- za ostvarivanje veze između grafičkog sučelja i logike aplikacije, potrebno je dodijeliti jedinstvene identifikatore
  - potrebna je @FXML anotacija

# Controller klasa
- predstavlja poslovnu logiku
- koriste varijable koje po tipu i nazivu odgovaraju elementima grafičkog sučelja
  - tako se mogu koristiti zajedničke značajke i atributi
- mora biti povezan grafičkim sučeljem korištenjem FXML konfiguracije
- svaka Controller klasa treba imati ***initialize*** metodu koja sadrži svu logiku za inicijalizaciju osnovnih postavki elemenata na grafičkom sučelju
  - metoda ne vraća ništa

# Anotacija FXML
- služi za označavanja elemenata u Javi koji se koriste unutar FXML datoteke

# Povezivanje metoda s elementima grafičkog sučelja
- za npr. gumb, potrebno je definirati ***onAction*** parametar
  - mora sadržavati ime metode koja se treba pozvati
- ako je potrebno prikazati zbirku podataka na grafičkom sučelju, JavaFX podržava ***Observable*** tipove zbirki koje omogućuju izravno praćenje promjena na grafičkom sučelju
  - ObservableList   
    - predstavlja listu koja omogućuje praćenje promjena na ekranu korištenjem Listeera
  - ObservableMap
    - predstavlja mapu koja omogućuje praćenje promjena na ekranu korištenjem Listeera
  - ListChangeListener i MapChangeListener
    - predstavljaju implementacije Listener klasa koje služe za primanje notifikacija koje generira objekt klase ***ObservableList*** i ***ObservableMap***
- osim toga postaje još i sljedeće korisne klase
  - FXCollections
    - sadrži istovjetne statičke metode onima u klasi ***java.util.Collections***
  - ListChangeListener.Change -> instance koje predstavljaju promjene nad ObservableList zbirkom
  - MapChangeListener.Change -> instance koje predstavljaju promjene nad ObservableMap zbirkom
  ```java
  // kreiranje ObservableListe i povezivanje s 
  ObservableList<Beverage> beverageObservableList = FXCollections.observableArrayList(beverageList);
  beverageTableView.setItems(beverageObservableList);
  ```

# Povezivanje stupaca tablice s varijablama u objektima
  ```java
  @FXML
  private TableColumn<Beverage, String> beverageNameColumn;

  ...

  public void initialize() {
    beverageNameColumn.setCellValueFactory(cellData ->
          new SimpleStringProperty(cellData.getValue().getName()));
  }
  ```

