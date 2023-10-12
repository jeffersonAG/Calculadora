module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencv;


    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora;
    exports com.example.calculadora.Arbol;
    opens com.example.calculadora.Arbol to javafx.fxml;
}