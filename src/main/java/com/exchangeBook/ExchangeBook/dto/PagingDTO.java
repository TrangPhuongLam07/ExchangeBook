package com.exchangeBook.ExchangeBook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
Create By : ANHTUAN
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingDTO <T>{
    private int page;
    private int size;
    private int totalItem;
    private int totalPage;

    List<T> models = new ArrayList<>();


}