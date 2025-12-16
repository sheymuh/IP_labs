package ru.ulstu.is.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.ulstu.is.server.entity.PlaylistEntity;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    Optional<PlaylistEntity> findOneByNameIgnoreCase(String name);
}