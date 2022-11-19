package by.rom.inventoryservice.repository;

import by.rom.inventoryservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByNameIn(String code);
}

