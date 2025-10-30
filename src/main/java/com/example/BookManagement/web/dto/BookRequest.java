package com.example.BookManagement.web.dto;

public record BookRequest(
			String title,
			String author,
			String category
	) { public void validate() {
	new RequestValidator().validate(this);
}}