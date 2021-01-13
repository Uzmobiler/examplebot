package uz.mobiler.examplebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mobiler.examplebot.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
