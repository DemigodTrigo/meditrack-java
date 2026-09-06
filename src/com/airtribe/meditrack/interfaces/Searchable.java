package com.airtribe.meditrack.interfaces;

public interface Searchable<T> {

    default boolean matches(String value, String searchText) {
        if (value == null || searchText == null) {
            return false;
        }

        return value.toLowerCase()
                .contains(searchText.toLowerCase());
    }
}