package hu.renew.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.renew.main.model.OperatingSystem;

@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Integer> {
	Page<OperatingSystem> findByNevContainingIgnoreCase(String nev, Pageable pageable);
}

