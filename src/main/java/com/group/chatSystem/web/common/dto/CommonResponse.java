package com.group.chatSystem.web.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommonResponse<T> {

    private Boolean success;
    private String message;
    private ResponseType type;
    private T content;

    public static CommonResponse createErrorResponse(Throwable e) {
        return CommonResponse.builder()
                             .success(false)
                             .message(e.getMessage())
                             .type(ResponseType.ERROR)
                             .build();
    }

    public static <T> CommonResponse<T> createVoidResponse() {
        return (CommonResponse<T>) CommonResponse.builder()
                                                 .success(true)
                                                 .message("success")
                                                 .type(ResponseType.NO_CONTENT)
                                                 .build();
    }

    public static <T> CommonResponse<T> createResponse(T content) {
        return (CommonResponse<T>) CommonResponse.builder()
                                                 .success(true)
                                                 .message("success")
                                                 .type(ResponseType.getResponseType(content))
                                                 .content(content)
                                                 .build();
    }

    public enum ResponseType {
        ERROR,
        SINGLE,
        LIST,
        SLICE,
        PAGE,
        NO_CONTENT
        ;

        public static <T> ResponseType getResponseType(T content) {
            if (content instanceof List) {
                return LIST;
            } else if (content instanceof Page) {
                return PAGE;
            } else if (content instanceof Slice<?>) {
                return SLICE;
            } else {
                return SINGLE;
            }
        }
    }
}
