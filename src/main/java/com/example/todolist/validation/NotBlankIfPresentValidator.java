package com.example.todolist.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // null = tidak diubah (valid). Jika ada, harus ada karakter non-spasi.
    return value == null || !value.trim().isEmpty();
  }
}
