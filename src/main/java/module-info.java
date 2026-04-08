/**
 * Module descriptor for Hotel Management System
 * 
 * Declares module dependencies for JavaFX and Java base libraries.
 * Required for Java 21+ module system (JPMS - Java Platform Module System)
 */
module HotelManagementSystem {
    // Require JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    
    // Require Java base functionality
    requires java.base;
    
    // Export main application package
    exports com.hotel;
}
