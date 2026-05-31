package ru.app.partmatcher.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.app.partmatcher.entity.*;
import ru.app.partmatcher.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PartRepository partRepository;
    private final AnalogPartRepository analogPartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0 || vehicleRepository.count() > 0 || partRepository.count() > 0) {
            return;
        }

        User admin = User.builder()
                .name("Administrator")
                .email("admin@partmatcher.local")
                .password(passwordEncoder.encode("admin123"))
                .roles(Set.of(Role.ADMIN, Role.SUPPORT))
                .build();

        User support = User.builder()
                .name("Support Agent")
                .email("support@partmatcher.local")
                .password(passwordEncoder.encode("support123"))
                .roles(Set.of(Role.SUPPORT))
                .build();

        // add demo user
        User demoUser = User.builder()
                .name("Demo User")
                .email("user@partmatcher.local")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(Role.USER))
                .build();

        userRepository.saveAll(List.of(admin, support, demoUser));

        Vehicle camry = Vehicle.builder()
                .vin("JT123456789012345")
                .brand("Toyota")
                .model("Camry")
                .year(2020)
                .engine("2.5L 4-Cylinder")
                .bodyType("Sedan")
                .build();

        Vehicle civic = Vehicle.builder()
                .vin("2HGFC2F69KH512345")
                .brand("Honda")
                .model("Civic")
                .year(2019)
                .engine("1.5L Turbo")
                .bodyType("Sedan")
                .build();

        Part toyotaOilFilter = Part.builder()
                .article("T-OF-001")
                .name("Toyota Oil Filter")
                .manufacturer("Toyota")
                .description("Оригинальный масляный фильтр для Toyota Camry")
                .price(BigDecimal.valueOf(18.90))
                .imageUrl("https://example.com/images/toyota-oil-filter.jpg")
                .category("Filter")
                .build();

        Part boschOilFilter = Part.builder()
                .article("B-OF-020")
                .name("Bosch Oil Filter")
                .manufacturer("Bosch")
                .description("Аналоговый масляный фильтр Bosch")
                .price(BigDecimal.valueOf(14.50))
                .imageUrl("https://example.com/images/bosch-oil-filter.jpg")
                .category("Filter")
                .build();

        Part mannOilFilter = Part.builder()
                .article("M-OF-033")
                .name("Mann Oil Filter")
                .manufacturer("Mann")
                .description("Аналоговый масляный фильтр Mann")
                .price(BigDecimal.valueOf(16.20))
                .imageUrl("https://example.com/images/mann-oil-filter.jpg")
                .category("Filter")
                .build();

        Part toyotaBrakePad = Part.builder()
                .article("T-BP-011")
                .name("Toyota Brake Pad")
                .manufacturer("Toyota")
                .description("Оригинальный тормозной колодки для Toyota Camry")
                .price(BigDecimal.valueOf(49.99))
                .imageUrl("https://example.com/images/toyota-brake-pad.jpg")
                .category("Brake")
                .build();

        Part hondaAirFilter = Part.builder()
                .article("H-AF-056")
                .name("Honda Air Filter")
                .manufacturer("Honda")
                .description("Оригинальный воздушный фильтр Honda Civic")
                .price(BigDecimal.valueOf(22.40))
                .imageUrl("https://example.com/images/honda-air-filter.jpg")
                .category("Filter")
                .build();

        partRepository.saveAll(List.of(toyotaOilFilter, boschOilFilter, mannOilFilter, toyotaBrakePad, hondaAirFilter));

        camry.getCompatibleParts().addAll(List.of(toyotaOilFilter, boschOilFilter, mannOilFilter, toyotaBrakePad));
        civic.getCompatibleParts().addAll(List.of(hondaAirFilter));

        vehicleRepository.saveAll(List.of(camry, civic));

        analogPartRepository.save(AnalogPart.builder()
                .originalPart(toyotaOilFilter)
                .analogPart(boschOilFilter)
                .build());

        analogPartRepository.save(AnalogPart.builder()
                .originalPart(toyotaOilFilter)
                .analogPart(mannOilFilter)
                .build());

        // ensure we have at least 20 vehicles and 100 parts and 20 analogs
        int existingVehicles = (int) vehicleRepository.count();
        int existingParts = (int) partRepository.count();
        int existingAnalogs = (int) analogPartRepository.count();

        List<Part> generatedParts = new java.util.ArrayList<>();
        List<Vehicle> generatedVehicles = new java.util.ArrayList<>();

        for (int i = existingVehicles + 1; i <= 20; i++) {
            String vin = String.format("VIN%014d", i);
            Vehicle v = Vehicle.builder()
                    .vin(vin)
                    .brand(i % 3 == 0 ? "Toyota" : i % 3 == 1 ? "Volkswagen" : "Ford")
                    .model("Model" + (i % 5 + 1))
                    .year(2010 + (i % 13))
                    .engine((i % 2 == 0) ? "1.6L" : "2.0L")
                    .bodyType((i % 2 == 0) ? "Sedan" : "Hatchback")
                    .build();
            generatedVehicles.add(v);
        }
        if (!generatedVehicles.isEmpty()) vehicleRepository.saveAll(generatedVehicles);

        java.util.List<String> manufacturers = List.of("Bosch", "NGK", "Valeo", "Mann", "Febi");
        java.util.List<String> categories = List.of("Brakes", "Filters", "Engine", "Suspension", "Electrical");

        for (int i = existingParts + 1; i <= 100; i++) {
            Part p = Part.builder()
                    .article(String.format("ART%06d", i))
                    .name("Part " + i)
                    .manufacturer(manufacturers.get(i % manufacturers.size()))
                    .description("Demo description for part " + i)
                    .price(BigDecimal.valueOf(10 + (i % 90)))
                    .imageUrl(null)
                    .category(categories.get(i % categories.size()))
                    .build();
            generatedParts.add(p);
        }
        if (!generatedParts.isEmpty()) partRepository.saveAll(generatedParts);

        // create analogs until we have 20
        java.util.Random rnd = new java.util.Random(42);
        java.util.List<Part> allParts = partRepository.findAll();
        while (analogPartRepository.count() < 20 && allParts.size() >= 2) {
            Part a = allParts.get(rnd.nextInt(allParts.size()));
            Part b = allParts.get(rnd.nextInt(allParts.size()));
            if (a.getId().equals(b.getId())) continue;
            analogPartRepository.save(AnalogPart.builder().originalPart(a).analogPart(b).build());
        }

        // assign parts to vehicles (compatibility)
        allParts = partRepository.findAll();
        java.util.List<Vehicle> allVehicles = vehicleRepository.findAll();
        for (int i = 0; i < allVehicles.size(); i++) {
            Vehicle v = allVehicles.get(i);
            for (int j = 0; j < 5; j++) {
                Part p = allParts.get((i * 5 + j) % allParts.size());
                v.getCompatibleParts().add(p);
                p.getCompatibleVehicles().add(v);
            }
            vehicleRepository.save(v);
        }
    }
}
