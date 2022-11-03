package product.order.cli.app.repository;

import product.order.cli.app.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

import static javax.persistence.LockModeType.*;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.number in :productNumbers" )
    List<Product> findByNumbers(@Param("productNumbers") Set<Long> productNumbers);
}
