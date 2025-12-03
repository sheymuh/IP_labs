package ru.ulstu.is.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.ulstu.is.server.entity.PlaylistEntity;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
}
