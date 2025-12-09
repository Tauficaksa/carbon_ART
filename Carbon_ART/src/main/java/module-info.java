module org.tauficaksa.carbon_art {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.tauficaksa.carbon_art to javafx.fxml;
    exports org.tauficaksa.carbon_art;
}