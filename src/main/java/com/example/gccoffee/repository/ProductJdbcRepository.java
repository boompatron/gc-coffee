package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.gccoffee.JdbcUtils.*;


@Repository
public class ProductJdbcRepository implements ProductRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public List<Product> findAll() {
        return namedParameterJdbcTemplate.query("select * from products", productRowMapper);
     }

    @Override
    public Product insert(Product product) {
        var update = namedParameterJdbcTemplate.update(
                "insert into products(product_id, product_name, category, price, description, created_at, updated_at) " +
                        "values(UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)"
                , toParamMap(product)
        );
        if (update != 1)
            throw new RuntimeException("Nothing was inserted");
        return product ;
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject("select * from products where product_id = UUID_TO_BIN(:productId)",
                            Collections.singletonMap("productId", id.toString().getBytes()), productRowMapper)
            );
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByName(String name) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject("select * from products where product_name = :name",
                            Collections.singletonMap("productName", name), productRowMapper)
            );
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
         return namedParameterJdbcTemplate.query("select * from products where category = :category",
                 Collections.singletonMap("category", category.toString()), productRowMapper);
    }

    @Override
    public void deleteAll() {

    }

    private final static RowMapper<Product> productRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));

        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Product product){
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreateAt());
        paramMap.put("updatedAt", product.getUpdateAt());
        return paramMap;
    }
}
