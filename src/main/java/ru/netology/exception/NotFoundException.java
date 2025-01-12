package ru.netology.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException() {
    super("Объект не найден.");
  }

  public NotFoundException(String message) {
    super(message);
  }
}
