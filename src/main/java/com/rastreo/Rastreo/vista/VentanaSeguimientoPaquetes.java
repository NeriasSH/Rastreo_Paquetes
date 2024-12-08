package com.rastreo.Rastreo.vista;


import com.rastreo.Rastreo.modelo.SeguimientoPaquete;
import com.rastreo.Rastreo.servicio.ServicioSeguimiento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaSeguimientoPaquetes extends JFrame {
    private JTable tablaSeguimientos;
    private DefaultTableModel modeloTabla;
    private ServicioSeguimiento servicioSeguimiento;
    private JButton botonActualizar;

    public VentanaSeguimientoPaquetes() {
        servicioSeguimiento = new ServicioSeguimiento();

        // Configuración de la ventana
        setTitle("Sistema de Seguimiento de Paquetes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar layout
        setLayout(new BorderLayout());

        // Configurar botón de actualización
        botonActualizar = new JButton("Actualizar");
        botonActualizar.addActionListener(e -> cargarSeguimientos());
        add(botonActualizar, BorderLayout.NORTH);

        // Configurar tabla
        String[] columnNames = {"Número Seguimiento", "Transportista", "Número Orden", "Estado Entrega"};
        modeloTabla = new DefaultTableModel(columnNames, 0);
        tablaSeguimientos = new JTable(modeloTabla);

        // Añadir tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaSeguimientos);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar seguimientos iniciales
        cargarSeguimientos();
    }

    private void cargarSeguimientos() {
        // Limpiar tabla actual
        modeloTabla.setRowCount(0);

        // Cargar seguimientos en un hilo separado
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                List<SeguimientoPaquete> seguimientos = servicioSeguimiento.obtenerSeguimientos();

                for (SeguimientoPaquete seguimiento : seguimientos) {
                    Object[] fila = {
                            seguimiento.getNumeroSeguimiento(),
                            seguimiento.getCodigoTransportista(),
                            seguimiento.getNumeroOrden(),
                            seguimiento.getEstadoEntrega()
                    };
                    modeloTabla.addRow(fila);
                }

                return null;
            }

            @Override
            protected void done() {
                // Notificar si hay un error
                try {
                    get();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VentanaSeguimientoPaquetes.this,
                            "Error al cargar seguimientos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
}