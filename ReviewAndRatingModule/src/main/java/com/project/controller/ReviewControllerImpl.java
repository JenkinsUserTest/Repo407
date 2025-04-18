package com.project.controller;

import com.project.dto.ReviewDTO;
import com.project.exception.*;
import com.project.service.ReviewService;
import com.project.service.ReviewServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReviewController is a REST controller that handles HTTP requests for managing reviews.
 * It provides endpoints to get, add, update, and delete reviews.
 * The controller uses Swagger annotations for API documentation and validation annotations for input validation.
 * @author Sabarish Iyer
 */

@SuppressWarnings("preview")
@RestController
@Validated
@RequestMapping("dbs/review")
public class ReviewControllerImpl implements ReviewController {
	
    private final ReviewService reviewService;

    @Autowired
    public ReviewControllerImpl(final ReviewService reviewService) {
    	this.reviewService=reviewService;
       // this.reviewService = SerializationUtils.clone(reviewService);
    }

    private static final Logger logger = LoggerFactory.getLogger(ReviewControllerImpl.class);

    /**
     * Retrieves success for redirecting.
     *
     * @return ResponseEntity containing the status OK.
     */
    @Override
    @GetMapping("/ok")
    public ResponseEntity<Boolean> getOk() {
        return ResponseEntity.ok(true);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId The ID of the review to retrieve.
     * @return ResponseEntity containing the review data or an error message.
     */
    @Override
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@Min(value = 1, message = "{com.project.dto.ReviewDTO.reviewid.min}") @PathVariable long reviewId) {
        ResponseEntity<ReviewDTO> response;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/dbs/review/ok");
        try {
            ReviewDTO reviewDTO = reviewService.retrieveReviewById(reviewId);
            response = new ResponseEntity<>(reviewDTO, headers, HttpStatus.FOUND);
        } catch (ReviewNotFoundException e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(new ReviewDTO(e.toString()), headers, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Retrieves all reviews.
     *
     * @return ResponseEntity containing a list of all reviews or an error message.
     */
    @Override
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        ResponseEntity<List<ReviewDTO>> response;
        try {
            List<ReviewDTO> reviewDTOList = reviewService.retrieveAllReviews();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/dbs/review/ok");
            response = new ResponseEntity<>(reviewDTOList, headers, HttpStatus.FOUND);
        } catch (ReviewNotFoundException e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO("No Reviews Found!")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO("No Reviews Found!")), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Retrieves all reviews by a specific user ID.
     *
     * @param userId The ID of the user whose reviews to retrieve.
     * @return ResponseEntity containing a list of reviews or an error message.
     */
    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByUserId(@Min(value = 1, message = "{com.project.dto.ReviewDTO.userid.min}") @PathVariable long userId) {
        ResponseEntity<List<ReviewDTO>> response;
        try {
            List<ReviewDTO> reviewDTOList = reviewService.retrieveAllReviewsByUserId(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/dbs/review/ok");
            response = new ResponseEntity<>(reviewDTOList, headers, HttpStatus.FOUND);
        } catch (ReviewNotFoundException e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO(STR."No Reviews with User ID: \"{userId} Found!")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO("No Reviews Found!")), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Retrieves all reviews by a specific book ID.
     *
     * @param bookId The ID of the user whose reviews to retrieve.
     * @return ResponseEntity containing a list of reviews or an error message.
     */
    @Override
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByBookId(@Size(min = 3, max = 20, message = "{com.project.dto.ReviewDTO.bookid.size}") @PathVariable String bookId) {
        ResponseEntity<List<ReviewDTO>> response;
        try {
            List<ReviewDTO> reviewDTOList = reviewService.retrieveAllReviewsByBookId(bookId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/dbs/review/ok");
            response = new ResponseEntity<>(reviewDTOList, headers, HttpStatus.FOUND);
        } catch (ReviewNotFoundException e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO(STR."No Reviews with Book ID: \{bookId} Found!")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(List.of(new ReviewDTO("No Reviews Found!")), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Adds a new review with specified rating, comment, user ID, and book ID.
     *
     * @param rating  The rating of the review.
     * @param comment The comment of the review.
     * @param userId  The ID of the user adding the review.
     * @param bookId  The ID of the book being reviewed.
     * @return ResponseEntity containing the created review or an error message.
     */
    @Override
    @PostMapping("/add/values")
    public ResponseEntity<ReviewDTO> addReview(
            @DecimalMin(value = "0.1", message = "{com.project.dto.ReviewDTO.rating.min}")
            @Max(value = 5, message = "{com.project.dto.ReviewDTO.rating.max}") @RequestParam float rating,
            @Size(min = 3, max = 200, message = "{com.project.dto.ReviewDTO.comment.size}") @Pattern(regexp = "^\\D.*", message = "{com.project.dto.ReviewDTO.comment.start}") @RequestParam String comment,
            @Min(value = 1, message = "{com.project.dto.ReviewDTO.userid.min}") @RequestParam long userId,
            @Size(min = 3, max = 200, message = "{com.project.dto.ReviewDTO.bookid.min}") @RequestParam String bookId) {
        ResponseEntity<ReviewDTO> response;
        ReviewDTO reviewDTO = null;
        try {
            reviewDTO = reviewService.addReview(rating, comment, userId, bookId);
            response = new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        } catch (UserNotFoundException | BookNotFoundException e) {
            logger.error(e.toString());
            response = new ResponseEntity<>(new ReviewDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.toString());
            response = new ResponseEntity<>(new ReviewDTO(e.getMessage()), HttpStatus.BAD_GATEWAY);
        }
        return response;
    }

    /**
     * Adds a new review using a ReviewDTO object.
     *
     * @param reviewDTO The ReviewDTO object containing review details.
     * @return ResponseEntity containing the created review or an error message.
     */
    @Override
    @PostMapping("/add")
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ResponseEntity<ReviewDTO> response;
        try {
            reviewDTO = reviewService.addReview(reviewDTO.getRating(), reviewDTO.getComment(), reviewDTO.getUserId(), reviewDTO.getBookId());
            response = new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        } catch (UserNotFoundException | BookNotFoundException e) {
            logger.error(e.toString());
            response = new ResponseEntity<>(new ReviewDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.toString());
            response = new ResponseEntity<>(new ReviewDTO(e.getMessage()), HttpStatus.BAD_GATEWAY);
        }
        return response;
    }

    /**
     * Updates an existing review by user ID.
     *
     * @param userId    The ID of the user updating the review.
     * @param reviewDTO The ReviewDTO object containing updated review details.
     * @return ResponseEntity containing the updated review or an error message.
     */
    @Override
    @PatchMapping("/update/{userId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @Min(value = 1, message = "{com.project.dto.ReviewDTO.userid.min}") @PathVariable long userId,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ResponseEntity<ReviewDTO> response;
        try {
            reviewDTO = reviewService.updateReview(userId, reviewDTO);
            response = new ResponseEntity<>(reviewDTO, HttpStatus.OK);
        } catch (UserNotAuthorizedException | UserNotFoundException | IDMismatchException | BookNotFoundException |
                 IllegalArgumentException e) {
            logger.error(e.toString());
            response = switch (e) {
                case UserNotAuthorizedException ex ->
                        new ResponseEntity<ReviewDTO>(new ReviewDTO(""), HttpStatus.UNAUTHORIZED);
                case UserNotFoundException ex ->
                        new ResponseEntity<>(new ReviewDTO(""), HttpStatus.NOT_FOUND);
                case BookNotFoundException ex ->
                        new ResponseEntity<>(new ReviewDTO(""), HttpStatus.NOT_FOUND);
                case IDMismatchException ex ->
                        new ResponseEntity<>(new ReviewDTO(""), HttpStatus.NOT_ACCEPTABLE);
                default ->
                        new ResponseEntity<>(new ReviewDTO(""), HttpStatus.BAD_REQUEST);
            };
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            response = new ResponseEntity<>(new ReviewDTO(""), HttpStatus.NOT_MODIFIED);
        }
        return response;
    }

    /**
     * Deletes a review by user ID and review ID.
     *
     * @param userId   The ID of the user deleting the review.
     * @param reviewId The ID of the review to be deleted.
     * @return ResponseEntity indicating the result of the delete operation.
     */
    @Override
    @DeleteMapping("/delete/{userId}/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(
            @Min(value = 1, message = "{com.project.dto.ReviewDTO.userid.min}") @PathVariable long userId,
            @Min(value = 1, message = "{com.project.dto.ReviewDTO.reviewid.min}") @PathVariable long reviewId) {
        ResponseEntity<Boolean> response = new ResponseEntity<>(true, HttpStatus.OK);
        try {
            if (!reviewService.deleteReview(userId, reviewId)) {
                response = new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
            }
        } catch (ReviewNotFoundException | UserNotAuthorizedException | UserNotFoundException |
                 IllegalArgumentException e) {
            logger.error(e.getMessage());
            response = switch (e) {
                case ReviewNotFoundException ex -> new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
                case UserNotFoundException ex -> new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
                case UserNotAuthorizedException ex -> new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
                default -> new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            };
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
        }
        return response;
    }
}