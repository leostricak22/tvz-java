package hr.java.restaurant.repository;

import hr.java.restaurant.model.Address;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class AddressRepository extends AbstractRepository<Address>{

    private final static String FILE_PATH = "dat/address.txt";

    @Override
    public Address findById(Long id) {
        return findAll().stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nepostojeći identifikator"));
    }

    @Override
    public Set<Address> findAll() {
        Set<Address> addresses = new HashSet<>();

        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            List<String> fileRows = stream.toList();

            while (!fileRows.isEmpty()) {
                Long id = Long.parseLong(fileRows.get(0));
                String street = fileRows.get(1);
                String houseNumber = fileRows.get(2);
                String city = fileRows.get(3);
                String postalCode = fileRows.get(4);

                addresses.add(new Address.Builder(id)
                        .setStreet(street)
                        .setHouseNumber(houseNumber)
                        .setCity(city)
                        .setPostalCode(postalCode)
                        .build());

                fileRows = fileRows.subList(5, fileRows.size());
            }
        } catch (Exception e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
        }

        return addresses;
    }

    @Override
    public void save(Set<Address> entity) {
        List<String> fileRows = new ArrayList<>();

        for (Address address : entity) {
            fileRows.add(address.getId().toString());
            fileRows.add(address.getStreet());
            fileRows.add(address.getHouseNumber());
            fileRows.add(address.getCity());
            fileRows.add(address.getPostalCode());
        }

        try {
            Files.write(Path.of(FILE_PATH), fileRows);
        } catch (Exception e) {
            System.err.println("Greška pri zapisivanju u datoteku: " + e.getMessage());
        }
    }

    @Override
    public Long findNextId() {
        return findAll().stream()
                .map(Address::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void save(Address address) {
        Set<Address> addresses = findAll();
        addresses.add(address);
        save(addresses);
    }
}
