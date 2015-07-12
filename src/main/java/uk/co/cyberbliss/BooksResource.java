package uk.co.cyberbliss;


import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.util.List;

public interface BooksResource {
    /**
     * @api {get} /api/book/:isbn Get a book via its ISBN code
     * @apiName getBookByIsbn
     * @apiGroup Books
     *
     * @apiParam {String} isbn The book's unique ISBN code
     *
     * @apiSampleRequest http://localhost:9080/api/book/111-1
     *
     * @apiSuccess {String} isbn Book's ISBN
     * @apiSuccess {String} title Title of book
     * @apiSuccess {String} author Book's author
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
    {
    "isbn": "111-1",
    "title": "Java 8 Lamdas",
    "author": "Richard Warburton",
    "_links": {
        "self": {
            "href": "http://localhost:9080/api/book/111-1"
        }
    }
    }

        @ApiError 404 Book not found
     *
     */
    ResponseEntity<Book> getBookByIsbn(String isbn);

    /**
     * @api {post} /api/book Add a book
     * @apiName addBook
     * @apiGroup Books
     *
     * @apiParam {String} isbn The book's unique ISBN code
     * @apiParam {String} title The book's title
     * @apiParam {String} author The book's author
     *
     * @apiParamExample {json} Request-Example:
    {
    "isbn": "111-31",
    "title": "Test",
    "author": "Steve",
    "_links": {
        "self": {
            "href": "http://localhost:9080/api/book/111-31"
        },
        "Books": {
            "href": "http://localhost:9080/api/books"
        }
    }
    }
        @apiSuccessExample {json} Success-Response:
      *     HTTP/1.1 201 CREATED
     */
    ResponseEntity<Book> addBook(Book book);

    /**
     * @api {get} /api/books Get a list of all books
     * @apiName getAllBooks
     * @apiGroup Books
     * @apiDescription Get a list of all books stored
     *
     * @apiSampleRequest http://localhost:9080/api/books
     *
     * @apiSuccess {String} isbn Book's ISBN
     * @apiSuccess {String} title Title of book
     * @apiSuccess {String} author Book's author
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *      [
    {
    "isbn": "111-1",
    "title": "Java 8 Lamdas",
    "author": "Richard Warburton",
    "_links": {
        "self": {
            "href": "http://localhost:9080/api/book/111-1"
        }
    }
    },
    {
    "isbn": "111-3",
    "title": "Test",
    "author": "Steve",
    "_links": {
        "self": {
            "href": "http://localhost:9080/api/book/111-3"
        }
    }
    }
            ]
     */
    ResponseEntity<List<Book>> getAllBooks();

    /**
     * @api {put} /api/book/:isbn Update a book
     * @apiName updateBook
     * @apiGroup Books
     * @apiDescription Update a book using its ISBN code
     * @apiParam {String} title The book's title
     * @apiParam {String} author The book's author
     *
     * @apiSuccess {String} isbn Book's ISBN
     * @apiSuccess {String} title Title of book
     * @apiSuccess {String} author Book's author
     *
     * @apiSuccessExample {json} Success-Response
     *      HTTP/1.1 200 OK
     *      {
    "isbn": "111-31",
    "title": "Test",
    "author": "Steve Austin",
    "_links": {
        "self": {
            "href": "http://localhost:9080/api/book/111-31"
        },
        "Books": {
            "href": "http://localhost:9080/api/books"
        }
    }
    }
     */
    ResponseEntity<Book> updateBook(String isbn, Book book);

    /**
     * @api {delete} /api/book/:isbn Delete a book
     * @apiName deleteBook
     * @apiGroup Books
     * @apiDescription Delete a book using its ISBN
     *
     * @apiParam {String} isbn The book's unique ISBN code
     *
     * @apiSampleRequest http://localhost:9080/api/book/111-1
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *
     */
    ResponseEntity<Void> deleteBook(String isbn);

}
