package dev.arielalvesdutra.api_instagram.controllers;

import dev.arielalvesdutra.api_instagram.controllers.dtos.CreateUserDTO;
import dev.arielalvesdutra.api_instagram.controllers.dtos.PostDTO;
import dev.arielalvesdutra.api_instagram.controllers.dtos.UpdateUserDTO;
import dev.arielalvesdutra.api_instagram.controllers.dtos.UserDTO;
import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.services.PostService;
import dev.arielalvesdutra.api_instagram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        User createdUser = userService.create(dto.toUser());
        URI uri = uriBuilder.path("/concepts/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new UserDTO(createdUser));
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> fetchByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);

        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @RequestMapping(path = "/{username}/posts", method = RequestMethod.GET)
    public ResponseEntity<Page<PostDTO>> fetchUserPostsByUsername(
            @PageableDefault(sort="id", page = 0, size = 10) Pageable pagination,
            @PathVariable String username) {
        Page<Post> posts = postService.findAllByAuthorUsername(pagination, username);

        return ResponseEntity.ok().body(PostDTO.toPage(posts));
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateByUsername(@PathVariable String username, @Valid @RequestBody UpdateUserDTO dto) {
        User updatedUser = userService.updateByUsername(username, dto.toUser());

        return ResponseEntity.ok().body(new UserDTO(updatedUser));
    }
}
