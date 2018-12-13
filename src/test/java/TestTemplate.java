import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestTemplate {

    @BeforeAll
    static void beforeAll() {}
    @BeforeEach
    void before() {}
    @AfterEach
    void after() {}
    @AfterAll
    static void afterAll() {}

    @Test
    void GLtest() {
        assertTrue(true);
    }
}
