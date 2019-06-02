package hotel.db.repository;

import hotel.db.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    public Long countByCreatedAfter(Date created);
}
