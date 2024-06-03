package com.zkt.zktspring.model;


import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response<T> {
    private Boolean success;
    private String message;
    private T data;
}
