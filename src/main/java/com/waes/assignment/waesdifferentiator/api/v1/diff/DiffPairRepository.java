package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffPairRepository extends JpaRepository<DiffPair, Long> {
}