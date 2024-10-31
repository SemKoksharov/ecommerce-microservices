package dev.semkoksharov.ecommerce.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String exception, String timestamp,String message, String stackTrace) {
}
