package com.example.signals;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import java.io.IOException;

public class signals extends Application{
    private double ampplitud = 1.0;
    private int frequency = 60;
    private double offset = 0.0;
    private double phase = 0.0;
    private int freq_m = 2000;
    private int sample_n = 50;
    private ComboBox<String> selectsignal;
    private TextField textamp;
    private TextField textfreq;
    private TextField textphase;
    private TextField textoffset;
    private TextField textsampfreq;
    private TextField text_n_sample;
    private LineChart<Number, Number> plotChart;
    private Button generar;
    private Button salir;
    @Override
    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();

        stage.setTitle("Signals graph");
        Pane root = new Pane();

        // Creamos el plot
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        plotChart = new LineChart<>(xAxis, yAxis);
        plot_seno();
        // agregar a la GUI

        Label namegraph = new Label("Grafica de señales");
        namegraph.setLayoutX(500);
        namegraph.setLayoutY(5);
        //--------------------Select Signal---------------------------------------------------
        Label selectS = new Label("Seleccione tipo de señal");
        selectS.setLayoutX(20);
        selectS.setLayoutY(20);
        selectsignal = new ComboBox<>();
        selectsignal.setItems(FXCollections.observableArrayList("Seno","Cuadrada", "Triangular"));
        selectsignal.setValue("Seno");
        selectsignal.setLayoutX(20);
        selectsignal.setLayoutY(40);
        //--------------------Input Amplitude-------------------------------------------------
        Label amplabel = new Label("Ingresa Amplitud");
        amplabel.setLayoutX(20);
        amplabel.setLayoutY(80);
        textamp = new TextField();
        textamp.setPromptText("Ingrese el valor");
        textamp.setLayoutX(20);
        textamp.setLayoutY(100);
        //--------------------Input Frequency-------------------------------------------------
        Label freqlabel = new Label("Ingrese frecuencia");
        freqlabel.setLayoutX(20);
        freqlabel.setLayoutY(150);
        textfreq = new TextField();
        textfreq.setPromptText("Ingrese el valor");
        textfreq.setLayoutX(20);
        textfreq.setLayoutY(170);
        //--------------------Input Phase-----------------------------------------------------
        Label phaselabel = new Label("Ingrese la Fase");
        phaselabel.setLayoutX(20);
        phaselabel.setLayoutY(210);
        textphase = new TextField();
        textphase.setPromptText("Ingrese el valor");
        textphase.setLayoutX(20);
        textphase.setLayoutY(230);
        //--------------------Input Offset----------------------------------------------------
        Label offsetlabel = new Label("Ingrese el Offset");
        offsetlabel.setLayoutX(20);
        offsetlabel.setLayoutY(270);
        textoffset = new TextField();
        textoffset.setPromptText("Ingrese el valor");
        textoffset.setLayoutX(20);
        textoffset.setLayoutY(290);
        //--------------------Input Sample Frequency------------------------------------------
        Label sampfreqlabel = new Label("Ingrese frecuencia de muestreo");
        sampfreqlabel.setLayoutX(20);
        sampfreqlabel.setLayoutY(330);
        textsampfreq = new TextField();
        textsampfreq.setPromptText("Ingrese el valor");
        textsampfreq.setLayoutX(20);
        textsampfreq.setLayoutY(350);
        //--------------------Input Sample Number---------------------------------------------
        Label n_samplelabel = new Label("Ingrese Número de muestras");
        n_samplelabel.setLayoutX(20);
        n_samplelabel.setLayoutY(390);
        text_n_sample = new TextField();
        text_n_sample.setPromptText("Ingrese el valor");
        text_n_sample.setLayoutX(20);
        text_n_sample.setLayoutY(410);
        //--------------------Buttons---------------------------------------------------------
        generar = new Button();
        generar.setText("Generar");
        generar.setOnAction(event -> update());
        generar.setLayoutX(20);
        generar.setLayoutY(600);
        salir = new Button();
        salir.setText("Salir");
        salir.setOnAction(actionEvent -> {
            Platform.exit();
        });
        salir.setLayoutX(110);
        salir.setLayoutY(600);

        //-----------------------------------------------------------------------------------
        root.getChildren().addAll(plotChart, selectS, selectsignal, textamp, amplabel, freqlabel, textfreq,
                phaselabel, textphase, offsetlabel, textoffset, sampfreqlabel, textsampfreq,
                n_samplelabel, text_n_sample, generar, salir, namegraph);
        Scene scene = new Scene(root, 980, 680);
        stage.setScene(scene);
        stage.show();
    }

    public boolean cell_empty(){
        return textamp.getText().isEmpty()
                || textfreq.getText().isEmpty()
                || textphase.getText().isEmpty()
                || textoffset.getText().isEmpty()
                || textsampfreq.getText().isEmpty()
                || text_n_sample.getText().isEmpty();
    }

    public void update(){
        if(!cell_empty()){
            try{
                ampplitud = Double.parseDouble(textamp.getText());
                frequency = Integer.parseInt(textfreq.getText());
                phase = Double.parseDouble(textphase.getText());
                offset = Double.parseDouble(textoffset.getText());
                freq_m = Integer.parseInt(textsampfreq.getText());
                sample_n = Integer.parseInt(text_n_sample.getText());
                change_signal();
            }catch (NumberFormatException e){
                showAlert("Error", "Los valores deben ser números válidos.");
            }
        }
        else {
            showAlert("Cuidado", "No puede dejar los cuadros de entrada de números vacíos.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void change_signal(){
        if (selectsignal.getValue().equals("Seno")){
            plot_seno();
        }
        if (selectsignal.getValue().equals("Cuadrada")){
            plot_rec();
        }
        if (selectsignal.getValue().equals("Triangular")){
            plot_sawtooth();
        }
    }

    public void plot_seno(){
        plotChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < sample_n; i++) {
            double t = i * 1.0 / freq_m;
            double sinValue = ampplitud * Math.sin(2 * Math.PI * frequency * t + phase * Math.PI / 180) + offset;
            series.getData().add(new XYChart.Data<>(t, sinValue));
        }
        plotChart.getData().add(series);
        plotChart.setLayoutX(250);
        plotChart.setLayoutY(30);
    }

    public void plot_sawtooth(){
        plotChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < sample_n; i++) {
            double t = i * 1.0 / freq_m;
            double sawtoothWaveValue = ampplitud * (2 * frequency * t + phase) + offset;
            series.getData().add(new XYChart.Data<>(t, sawtoothWaveValue));
        }
        plotChart.getData().add(series);
        plotChart.setLayoutX(250);
        plotChart.setLayoutY(30);
    }

    public void plot_rec(){
        plotChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < sample_n; i++) {
            double t = i * 1.0 / freq_m;
            double sawtoothWaveValue = ampplitud * Math.floor(frequency*t + phase)%2 + offset; // Fórmula de la señal diente de sierra
            series.getData().add(new XYChart.Data<>(t, sawtoothWaveValue));
        }
        plotChart.getData().add(series);
        plotChart.setLayoutX(250);
        plotChart.setLayoutY(30);
    }

    public static void main(String[] args){
        launch();
    }
}
