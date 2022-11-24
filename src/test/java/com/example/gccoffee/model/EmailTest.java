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
        assertEquals(email.getAddress(), "a@email.com");
    }

    @Test
    void emailEqTest(){
        var email1 = new Email("a@email.com");
        var email2 = new Email("a@email.com");
        assertEquals(email1, email2);
    }
}