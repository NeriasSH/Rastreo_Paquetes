package com.rastreo.Rastreo;

import com.rastreo.Rastreo.vista.VentanaSeguimientoPaquetes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class RastreoApplication {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			VentanaSeguimientoPaquetes ventana = new VentanaSeguimientoPaquetes();
			ventana.setVisible(true);
		});
	}

	//Esto es una prueba

}
