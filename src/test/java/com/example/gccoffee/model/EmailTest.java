package com.example.gccoffee.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    void InvalidEmailTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            var email = new Email("abcdefg");
        });
    }

    @Test
    void ValidEmailTest(){
        var email = new Email("a@email.com");
        assertTrue(email.getAddress() == "a@email.com");
    }
}