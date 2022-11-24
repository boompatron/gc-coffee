package com.example.gccoffee.model;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private final String address;

    public Email(String address) {
        Assert.notNull("address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50, "address length should be 4 ~ 50");
        Assert.isTrue(checkAddress(address), "Invalid address ");
        this.address = address;
    }

    public static boolean checkAddress(String address){
        return Pattern.matches("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", address);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Email email = (Email) obj;
        return address.equals(email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("address = '").append(address).append('\'');
        sb.append("}");

        return sb.toString();
    }

    public String getAddress() {
        return address;
    }
}
