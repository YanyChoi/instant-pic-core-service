package com.instantpic.coreservice.controller;

import com.instantpic.coreservice.dto.comment.CommentDto;
import com.instantpic.coreservice.dto.comment.CommentList;
import com.instantpic.coreservice.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentDto postComment(@RequestBody CommentDto comment) {
        CommentDto result = commentService.postComment(comment);
        return result;
    }

    @DeleteMapping
    public CommentList deleteComment(Optional<Integer> commentId, Optional<Integer> articleId) {
        CommentList result = commentService.deleteComment(commentId, articleId);
        return result;
    }

    @GetMapping
    public CommentList getComments(int articleId) {
        CommentList result = commentService.getComments(articleId);
        return result;
    }
}
