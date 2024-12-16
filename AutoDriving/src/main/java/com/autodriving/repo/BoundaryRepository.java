package com.autodriving.repo;

import com.autodriving.model.Boundary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoundaryRepository extends JpaRepository<Boundary, Long> {
}
