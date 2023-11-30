package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.service.CommentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }
    //localhost:8080/api/comments?postId=1
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestParam(name = "postId") long postId,@RequestBody CommentDto comment){
        return new ResponseEntity<>(this.commentService.createComment(postId,comment), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCommentById(@RequestParam(name="commentId") long commentId,@RequestParam(name="postId") long postId){
        this.commentService.deleteCommentById(commentId,postId);
        return new ResponseEntity<>("comment deleted successfully!",HttpStatus.OK);
    }

    @GetMapping
    public List<CommentDto> findCommentsById(@RequestParam(name = "postId") long postId){
        List<CommentDto> responseList=this.commentService.findCommentsById(postId);
        return responseList;
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestParam(name="commentId") long commentId,@RequestBody CommentDto comment){
        return new ResponseEntity<>(this.commentService.updateComment(commentId,comment),HttpStatus.OK);
    }
}
