package org.bigs.util.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommonResponse<T> {
    private int code;
    private String message;
    private boolean success;
    private T data;

    @Builder
    public CommonResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }


}
