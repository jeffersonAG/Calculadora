module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
<<<<<<< Updated upstream
=======
    requires opencv;



	requires javafx.graphics;

    requires tess4j;
    requires java.desktop;
	requires com.opencsv;

>>>>>>> Stashed changes


    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora;
}