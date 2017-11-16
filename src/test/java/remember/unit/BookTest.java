package remember.unit;

import org.junit.*;
import remember.domain.Book;

public class BookTest {

    @Test
    public void getterAndSetterTest(){

        Book book = new Book(null);
        Assert.assertEquals(null, book.getName());

        book.setName("course book");
        Assert.assertEquals("course book", book.getName());
    }
}