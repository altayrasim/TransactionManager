module com.example.gui_programming {
    requires javafx.controls;
    requires javafx.fxml;


    opens banking_gui to javafx.fxml;
    exports banking_gui;
}