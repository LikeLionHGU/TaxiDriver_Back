package hgu.likelion.fish.commons.image.repository;

import hgu.likelion.fish.commons.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<Image, Long> {
}
