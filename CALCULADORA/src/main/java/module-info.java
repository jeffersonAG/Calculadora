module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;



	requires javafx.graphics;

    requires tess4j;
    requires java.desktop;



    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora.Arbol;
    opens com.example.calculadora.Arbol to javafx.fxml;
    exports com.example.calculadora.interfaz;
    opens com.example.calculadora.interfaz to javafx.fxml;
    exports com.example.calculadora.reconocimiento_de_patrones;
    opens com.example.calculadora.reconocimiento_de_patrones to javafx.fxml;
    exports com.example.calculadora;

}