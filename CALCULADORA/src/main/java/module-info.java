module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;
	requires javafx.graphics;


    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora;
    opens com.example.calculadora.Arbol to java. fx.fxml;
    exports com.example.calculadora.Arbol;
    exports com.example.calculadora.Reconocimiento_de_Patrones;
    opens com.example.calculadora.Reconocimiento_de_Patrones to javafx.fxml;
    exports com.example.calculadora.Interfaz;
    opens com.example.calculadora.Interfaz to javafx.fxml;

}