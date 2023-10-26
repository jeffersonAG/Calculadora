module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;



	requires javafx.graphics;

    requires tess4j;
    requires java.desktop;
	requires com.opencsv;



    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora.Arbol;
    opens com.example.calculadora.Arbol to javafx.fxml;
    exports com.example.calculadora.Interfaz;
    opens com.example.calculadora.interfaz to javafx.fxml;
    exports com.example.calculadora.Reconocimiento_de_Patrones;
    opens com.example.calculadora.reconocimiento_de_patrones to javafx.fxml;
    exports com.example.calculadora;

}