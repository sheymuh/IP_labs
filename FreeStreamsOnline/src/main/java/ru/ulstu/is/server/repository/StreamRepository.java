package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.ulstu.is.server.entity.StreamEntity;

public interface StreamRepository extends JpaRepository<StreamEntity, Long> {
}
