package com.airtribe.meditrack.interfaces;

public interface Searchable<T> {

    T searchById(long id);

    default boolean matches(String value, String searchText) {
        if (value == null || searchText == null) {
            return false;
        }

        return value.toLowerCase()
                .contains(searchText.toLowerCase());
    }
}