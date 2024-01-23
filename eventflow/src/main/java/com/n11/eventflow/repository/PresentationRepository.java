package com.n11.eventflow.repository;

import com.n11.eventflow.model.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PresentationRepository extends JpaRepository<Presentation, UUID> {
}
