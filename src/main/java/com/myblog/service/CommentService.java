package com.myblog.service;

import com.myblog.entity.Comment;
import com.myblog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId,CommentDto comment);

    void deleteCommentById(long commentId,long postId);

    List<CommentDto> findCommentsById(long postId);

    CommentDto updateComment(long commentId,CommentDto comment);
}
