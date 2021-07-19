package musicstore;

import org.springframework.data.repository.CrudRepository;

import musicstore.models.Product;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ProductRepository extends CrudRepository<Product, String> {

	Product findOneByCode(String productCode);
	
}