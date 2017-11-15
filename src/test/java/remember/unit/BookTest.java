package remember.unit;

import org.junit.*;
import remember.Domain.Book;

public class BookTest {

    public BookTest(){

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getterAndSetterTest(){

        Book book = new Book(null);

        Assert.assertEquals(null, book.getName());

        book.setName("course book");

        Assert.assertEquals("course book", book.getName());
    }
}