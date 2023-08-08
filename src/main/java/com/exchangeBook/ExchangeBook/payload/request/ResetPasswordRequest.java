package com.exchangeBook.ExchangeBook.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

	private String tokenOrCurrentPassword;
	private String newPassword;
}
