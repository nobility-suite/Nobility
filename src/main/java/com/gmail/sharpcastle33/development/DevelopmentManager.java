package com.gmail.sharpcastle33.development;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentManager {
    private List<Class> types; // List of all types of developments

    public DevelopmentManager() {
        types = new ArrayList<>();
    } // constructor

    /**
     * Register a Development with Nobility
     * @param type Class type of Development
     */
    public void registerDevelopment(Class type) {
        types.add(type);
    } // registerDevelopment

    public List<Class> getTypes() {
        return types;
    } // getTypes
} // class
