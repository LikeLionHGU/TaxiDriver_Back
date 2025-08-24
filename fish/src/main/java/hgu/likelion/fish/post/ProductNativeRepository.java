package hgu.likelion.fish.post;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductNativeRepository {
    private final EntityManager em;

    public List<ProductDto> findLatest2ByName(String name) {
        String sql = """
            SELECT created_at, name, location, size, packaging, `count`,
                   highest_price, lowest_price, average_price
            FROM products
            WHERE name = :name
            ORDER BY created_at DESC
            LIMIT 2
        """;
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("name", name)
                .getResultList();

        return rows.stream().map(r -> new ProductDto(
                ((Timestamp) r[0]).toLocalDateTime(),
                (String) r[1],
                (String) r[2],
                (String) r[3],
                (String) r[4],
                ((Number) r[5]).longValue(),
                ((Number) r[6]).longValue(),
                ((Number) r[7]).longValue(),
                ((Number) r[8]).longValue()
        )).toList();
    }
}
