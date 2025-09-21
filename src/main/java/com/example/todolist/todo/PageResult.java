package com.example.todolist.todo;

import java.util.List;

public record PageResult<T>(List<T> items, int total) {}
