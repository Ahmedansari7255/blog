package com.myblog.controller;

import com.myblog.payload.PostDetails;
import com.myblog.payload.PostDto;
import com.myblog.service.PostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostServiceImpl service;
    public PostController(PostServiceImpl service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postdto, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto=service.savePost(postdto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public PostDetails viewPosts(@RequestParam(name="pageNo",required=false,defaultValue="0") int pageNo,
                                 @RequestParam(name="pageSize",required=false,defaultValue ="5") int pageSize,
                                 @RequestParam(name="sortBy",required = false,defaultValue = "id") String sortBy,
                                 @RequestParam(name="sortDir",required = false,defaultValue = "asc") String sortDir)
    {
       return service.viewPost(pageNo,pageSize,sortBy,sortDir);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable(name="id") long id){
        return service.getPostById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name="id") long id,@RequestBody PostDto dto){
        return new ResponseEntity<>(service.updatePost(id,dto),HttpStatus.OK) ;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name="id") long id){
        service.deletePost(id);
        return  new ResponseEntity<>("post deleted successfully!",HttpStatus.OK);
    }

}
