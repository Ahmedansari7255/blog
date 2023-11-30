package com.myblog.service;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements  CommentService{

    private PostRepository postRepo;
    private CommentRepository commentRepo;

    private ModelMapper modelMapper;
    public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo,ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.modelMapper=modelMapper;
    }

    public Comment mapToEntity(CommentDto dto){
        Comment comment = modelMapper.map(dto, Comment.class);
        return  comment;
    }

    public CommentDto mapToDto(Comment comment){
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
        return dto;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto comment) {
        Post post =postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("post not found with id"+postId));

        Comment comment1=mapToEntity(comment);
        comment1.setPost(post);
        Comment comment2=commentRepo.save(comment1);
        CommentDto response=mapToDto(comment2);
        return response;

    }

    @Override
    public void deleteCommentById(long commentId, long postId) {
        Post post=postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("post not found with id"+postId));
        commentRepo.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findCommentsById(long postId) {
        List<Comment> commentList=commentRepo.findByPostId(postId);
        List<CommentDto> dtos=commentList.stream().map(this::mapToDto).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto comment) {
        Comment existing=commentRepo.findById(commentId).get();
        existing.setEmail(comment.getEmail());
        existing.setBody(comment.getBody());
        existing.setName(comment.getName());

        Comment updated=commentRepo.save(existing);

        CommentDto dto=mapToDto(updated);
        return dto;
    }
}
