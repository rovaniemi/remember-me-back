package remember.unit;

import org.junit.*;
import remember.domain.inertances.Book;

public class BookTest {

    @Test
    public void getterAndSetterTest(){

        Book book = new Book();
        Assert.assertEquals(null, book.getTitle());

        book.setTitle("course book");
        Assert.assertEquals("course book", book.getTitle());
    }
}