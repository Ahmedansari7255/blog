package com.myblog.service;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.PostDetails;
import com.myblog.payload.PostDto;
import com.myblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository repo;

    @Override
    public PostDto savePost(PostDto postDto) {
        Post post=mapToEntity(postDto);
        Post savedPost=repo.save(post);
        PostDto dto=mapToDto(savedPost);
        return dto;
    }
    @Override
    public PostDetails viewPost(int pageNo, int pageSize,String sortBy,String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable=PageRequest.of(pageNo,pageSize,sort);
        Page<Post> page =repo.findAll(pageable);
        List<Post> postList=page.getContent();
        List<PostDto> dtos=postList.stream().map(this::mapToDto).collect(Collectors.toList());

        PostDetails response=new PostDetails();
        response.setDto(dtos);
        response.setPageNo(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post=repo.findById(id).get();
        PostDto dto=mapToDto(post);
        return dto;
    }

    @Override
    public PostDto updatePost(long id, PostDto dto) {
        Post post=repo.findById(id).get();

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());

        Post upPost=repo.save(post);

        PostDto updatedPost=mapToDto(upPost);
        return updatedPost;
    }

    @Override
    public void deletePost(long id) {
        Post post=repo.findById(id).orElseThrow(()->new ResourceNotFound("Id not found!"));
        repo.deleteById(id);
    }


    Post mapToEntity(PostDto postDto){
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    PostDto mapToDto(Post post){
        PostDto postDto=new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }
}

