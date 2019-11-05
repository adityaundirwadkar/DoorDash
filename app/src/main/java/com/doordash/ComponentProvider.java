package com.doordash;

/**
 *
 */
public class ComponentProvider {

    private static DoorDashComponent sComponent;

    public static DoorDashComponent get() {
        return sComponent;
    }

    public static void set(DoorDashComponent owlComponent) {
        sComponent = owlComponent;
    }
}
