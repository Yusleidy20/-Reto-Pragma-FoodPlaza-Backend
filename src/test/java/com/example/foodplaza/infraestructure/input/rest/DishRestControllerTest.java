package com.example.foodplaza.infraestructure.input.rest;


import com.example.foodplaza.infrastructure.input.rest.DishRestController;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = DishRestController.class)
@ExtendWith(MockitoExtension.class)
class DishRestControllerTest {

}
