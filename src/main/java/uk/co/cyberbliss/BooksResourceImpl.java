package uk.co.cyberbliss;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@Api(value = "Book", description = "Operations about Books")
public class BooksResourceImpl implements BooksResource {

    @Autowired
    private BookRepository booksRepo;

    @Override
    @RequestMapping(value="/book/{isbn}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Get a book using its ISBN")
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
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a book")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
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
    @ApiOperation(value = "List all books")
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
    @ApiOperation(value = "Update a book")
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
    @ApiOperation(value = "Delete a book")
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
