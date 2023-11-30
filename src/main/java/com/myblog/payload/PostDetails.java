package com.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetails {

    private List<PostDto> dto;
    private int pageNo;
    private int PageSize;
    private int totalPages;
    private boolean lastPage;

}
