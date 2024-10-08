package com.example.ebookstore.Controller;

import com.example.ebookstore.Entity.Book;
import com.example.ebookstore.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v2/books")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174"})
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("add")
    public ResponseEntity<Object> createBooks(@RequestBody Book books){
        //Provide json according to format of Book entity(see the entity ok!!)
        try{
            Book createdBook=bookService.createBook(books);
            //Create book provided from json using repository in service
            //May produce exception(IllegalArgument)
            String successMessage="New Book was added to the system.";
            Map<String,Object> response=new HashMap<>();
            //Create hashmap to put multiple data ;here successmessage and book
            response.put("message",successMessage);
            response.put("books",createdBook);
            //fill hashmap
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            String errorMessage=e.getMessage();
            //in case of error get the error message
            Map<String,Object> errorResponse= new HashMap<>();
            errorResponse.put("message",errorMessage);
            //fill hashmap with response(error)
            return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
        }
    }

    @GetMapping("getAll")
    public List<Book> getAllBooks(){
        //Should this api be present here?
        return bookService.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Object> getBookById(@PathVariable long bookId){
        try{
            //orElseThrow() method from optional class to handle null values
            Book book= bookService.getBookById(bookId).orElseThrow(()->new RuntimeException("Book with id not found"));
            return new ResponseEntity<>(book,HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{bookId}")
    public ResponseEntity<Object> deleteBookById(@PathVariable Long bookId) {
        try {
            bookService.deleteBook(bookId);
            //void method may produce illegal argument exception when findById fails
            Map<String, String> response = new HashMap<>();
            response.put("Message", "Book deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            //Catch error from service
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("Message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("edit/{bookId}")
    public ResponseEntity<Object> putBook(@PathVariable Long bookId,@RequestBody Book updatedBook){
        try {
            Book changedBook=bookService.putBook(bookId,updatedBook);
            //since optional<book> is returned by repository we need to catch error
            return new ResponseEntity<>(changedBook,HttpStatus.OK);
        }catch (IllegalArgumentException e){
            Map<String,String> errorResponse=new HashMap<>();
            errorResponse.put("message",e.getMessage());
            return new ResponseEntity<>(errorResponse,HttpStatus.OK);
        }
    }

}
