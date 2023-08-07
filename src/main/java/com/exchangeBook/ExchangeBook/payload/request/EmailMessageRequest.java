package com.exchangeBook.ExchangeBook.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageRequest {
    private String title;
    private String content;
    private long idPost;
}
