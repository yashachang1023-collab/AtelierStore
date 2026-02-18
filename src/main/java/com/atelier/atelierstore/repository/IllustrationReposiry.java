package com.atelier.atelierstore.repository;

import com.atelier.atelierstore.model.Illustration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IllustrationReposiry extends JpaRepository<Illustration, String> {
}
