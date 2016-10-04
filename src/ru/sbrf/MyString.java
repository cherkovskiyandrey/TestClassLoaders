package ru.sbrf;

import java.util.Objects;

public class MyString implements Stringable{

    private final String base;

    public MyString(String base) {
        this.base = base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyString myString = (MyString) o;
        return Objects.equals(base, myString.base);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base);
    }

    @Override
    public String getStr() {
        return base;
    }
}
