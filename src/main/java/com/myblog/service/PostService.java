package com.myblog.service;

import com.myblog.payload.PostDetails;
import com.myblog.payload.PostDto;

import java.util.List;

public interface PostService {
    PostDto savePost(PostDto postDto);
    PostDetails viewPost(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(long id,PostDto dto);

    void deletePost( long id);
}
