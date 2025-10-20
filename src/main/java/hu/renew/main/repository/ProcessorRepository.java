package hu.renew.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.renew.main.model.Processor;

@Repository
public interface ProcessorRepository extends JpaRepository<Processor, Integer> {
    Page<Processor> findByGyartoIgnoreCase(String gyarto, Pageable pageable);
    Page<Processor> findByTipusContainingIgnoreCase(String tipus, Pageable pageable);
}

