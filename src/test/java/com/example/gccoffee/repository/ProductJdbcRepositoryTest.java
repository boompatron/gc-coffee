package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test" )
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductJdbcRepositoryTest {

    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp(){
        var config = aMysqldConfig(v8_0_11)
                .withCharset(UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order_mgmt", ScriptResolver.classPathFile("schema.sql"))
                .start();
    }

    @AfterAll
    void cleanUp(){
        embeddedMysql.stop();
    }

    @Autowired
    ProductRepository repository;

    final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKEAGE, 1000L);

    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다")
    void insertTest(){
        repository.insert(newProduct);
        var all = repository.findAll();
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @Order(2)
    @DisplayName("이름으로 상품 조회 가능 테스트")
    void findByNameTest(){
        var product = repository.findByName(newProduct.getProductName());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(3)
    @DisplayName("ID로 상품 조회 가능 테스트")
    void findByIdTest(){
        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("카테고리로 상품 조회 가능 테스트")
    void findByCategoryTest(){
        var product = repository.findByCategory(Category.COFFEE_BEAN_PACKEAGE);
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(5)
    @DisplayName("수정 테스트")
    void updateTest(){
        newProduct.setProductName("updated-name");
        repository.update(newProduct);

        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @Order(6)
    @DisplayName("전부다 삭제")
    void deleteAllTest(){
        repository.deleteAll();
        var afterDeleteAll = repository.findAll();
        assertThat(afterDeleteAll.isEmpty(), is(true));
    }

}