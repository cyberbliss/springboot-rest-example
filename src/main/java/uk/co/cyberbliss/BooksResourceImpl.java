package uk.co.cyberbliss;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class BooksResourceImpl implements BooksResource {

    @Autowired
    private BookRepository booksRepo;

    @Override
    @RequestMapping(value="/book/{isbn}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book result = booksRepo.getBook(isbn);
        HttpStatus httpStatus;
        if(result != null) {
            result.removeLinks();
            result.add(linkTo(methodOn(BooksResourceImpl.class).getBookByIsbn(isbn)).withSelfRel());
            httpStatus = HttpStatus.OK;
        }else{
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<Book>(result, httpStatus);

    }


    @Override
    @RequestMapping(value = "/book",
            method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        booksRepo.addBook(book);
        book.add(linkTo(methodOn(BooksResourceImpl.class).getBookByIsbn(book.getIsbn())).withSelfRel());
        book.add(linkTo(methodOn(BooksResourceImpl.class).getAllBooks()).withRel("Books"));

        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @RequestMapping(value="/books",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Override
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = booksRepo.getAllBooks();
        books.forEach(book -> {
            book.removeLinks();
            book.add(linkTo(methodOn(BooksResourceImpl.class).getBookByIsbn(book.getIsbn())).withSelfRel());
        });
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/book/{isbn}",
            method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable String isbn, @RequestBody Book book) {
        HttpStatus httpStatus;
        if(booksRepo.isBookAvailable(isbn)){
            book.setIsbn(isbn);
            booksRepo.addBook(book);
            httpStatus = HttpStatus.OK;
            book.add(linkTo(methodOn(BooksResourceImpl.class).getBookByIsbn(isbn)).withSelfRel());
            book.add(linkTo(methodOn(BooksResourceImpl.class).getAllBooks()).withRel("Books"));
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(book,httpStatus);
    }

    @Override
    @RequestMapping(value = "/book/{isbn}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        HttpStatus httpStatus;
        if(booksRepo.removeBook(isbn)){
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(httpStatus);
    }
}
