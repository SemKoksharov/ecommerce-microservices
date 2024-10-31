package dev.semkoksharov.ecommerce.dto;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationError(Map<String,String> errorMap, HttpStatus status, String timestamp) {
}
