package dev.arielalvesdutra.api_instagram.controllers.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class TogglePostLikeDTO {
    @NotNull
    private Long userId;
}
