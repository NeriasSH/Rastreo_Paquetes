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
    private JButton botonBuscar;
    private JTextField campoBusqueda;
    private JComboBox<String> comboBusquedaPor;

    public VentanaSeguimientoPaquetes() {
        servicioSeguimiento = new ServicioSeguimiento();

        // Configuración de la ventana
        setTitle("Sistema de Seguimiento de Paquetes");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        // Panel superior para búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Selector de tipo de búsqueda
        comboBusquedaPor = new JComboBox<>(new String[]{"Tracking Number", "Courier Code"});
        comboBusquedaPor.setPreferredSize(new Dimension(150, 30));
        panelBusqueda.add(new JLabel("Buscar por: "));
        panelBusqueda.add(comboBusquedaPor);

        // Campo de búsqueda
        campoBusqueda = new JTextField(20);
        campoBusqueda.setPreferredSize(new Dimension(250, 30));
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusqueda.setToolTipText("Ingrese Tracking Number o Courier Code");
        panelBusqueda.add(campoBusqueda);

        // Botón de búsqueda
        botonBuscar = new JButton("Buscar");
        botonBuscar.setPreferredSize(new Dimension(100, 30));
        botonBuscar.addActionListener(e -> buscarSeguimiento());
        panelBusqueda.add(botonBuscar);

        // Configurar tabla
        String[] columnNames = {"Número Seguimiento", "Transportista", "Número Orden", "Estado Entrega"};
        modeloTabla = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla no editable
            }
        };
        tablaSeguimientos = new JTable(modeloTabla);

        // Personalizar tabla
        tablaSeguimientos.setRowHeight(25);
        tablaSeguimientos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Añadir componentes al panel principal
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tablaSeguimientos), BorderLayout.CENTER);

        // Añadir padding
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar al frame
        add(panelPrincipal);
    }

    private void buscarSeguimiento() {
        // Validar campo de búsqueda
        String textoBusqueda = campoBusqueda.getText().trim();
        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor ingrese un valor de búsqueda",
                    "Búsqueda Inválida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Determinar tipo de búsqueda
        String tipoBusqueda = (String) comboBusquedaPor.getSelectedItem();

        // Limpiar tabla actual
        modeloTabla.setRowCount(0);

        // Cargar seguimientos en un hilo separado
        SwingWorker<List<SeguimientoPaquete>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<SeguimientoPaquete> doInBackground() throws Exception {
                if (tipoBusqueda.equals("Tracking Number")) {
                    return servicioSeguimiento.obtenerSeguimientosPorTracking(textoBusqueda);
                } else {
                    return servicioSeguimiento.obtenerSeguimientosPorCourier(textoBusqueda);
                }
            }

            @Override
            protected void done() {
                try {
                    List<SeguimientoPaquete> seguimientos = get();

                    if (seguimientos.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                VentanaSeguimientoPaquetes.this,
                                "No se encontraron resultados para la búsqueda",
                                "Sin Resultados",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        return;
                    }

                    for (SeguimientoPaquete seguimiento : seguimientos) {
                        Object[] fila = {
                                seguimiento.getNumeroSeguimiento(),
                                seguimiento.getCodigoTransportista(),
                                seguimiento.getNumeroOrden(),
                                seguimiento.getEstadoEntrega()
                        };
                        modeloTabla.addRow(fila);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            VentanaSeguimientoPaquetes.this,
                            "Error al buscar seguimientos: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}