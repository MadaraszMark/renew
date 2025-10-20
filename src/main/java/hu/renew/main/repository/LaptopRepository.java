package hu.renew.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.renew.main.model.Laptop;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Integer> {
    Page<Laptop> findByGyartoIgnoreCase(String gyarto, Pageable pageable);
    Page<Laptop> findByTipusContainingIgnoreCase(String tipus, Pageable pageable);
    Page<Laptop> findByArLessThanEqual(Integer ar, Pageable pageable);
    Page<Laptop> findByDbGreaterThan(Integer db, Pageable pageable);
}

