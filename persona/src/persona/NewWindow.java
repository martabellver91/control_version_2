package persona;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public final class NewWindow extends JFrame implements ActionListener {

    static Connection conexion = null;
    static Statement sentencia = null;
    static ResultSet resultado = null;
    static PreparedStatement ps;
    static JTable tbl_employees;
    static JButton btn_modificar;
    static JTextField txt_nombre, txt_sexo;
    static JLabel lbl_nombre, lbl_sexo;
    static int filastotales;

    public NewWindow(JFrame frame) {
        values();
        init();
        panel();
        asign();
    }

    public void init() {
        txt_sexo = new JTextField();
        txt_sexo.setColumns(10);
        txt_nombre = new JTextField();
        txt_nombre.setColumns(10);
        lbl_nombre = new JLabel("Nombre:");
        lbl_sexo = new JLabel("Sexo:");
        btn_modificar = new JButton("Modificar");
        btn_modificar.addActionListener(this);
        tbl_employees = new JTable(new MyTablemod(filastotales));
        tbl_employees.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tbl_employees.setFillsViewportHeight(true);
    }

    public void panel() {
        JScrollPane scrollPane = new JScrollPane(tbl_employees);
        tbl_employees.setFillsViewportHeight(true);
        Panel panel1 = new Panel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        panel1.add(lbl_nombre, c);
        c.gridx = 2;
        c.gridy = 0;
        panel1.add(lbl_sexo, c);
        c.gridx = 1;
        c.gridy = 1;
        panel1.add(txt_nombre, c);
        c.gridx = 2;
        c.gridy = 1;
        panel1.add(txt_sexo, c);
        c.gridx = 1;
        c.gridy = 2;
        panel1.add(btn_modificar, c);

        add(scrollPane, BorderLayout.CENTER);
        add(panel1, BorderLayout.SOUTH);
    }

    public static void values() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/EJEMPLO", "root", "");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery("SELECT COUNT(*) FROM persona");
            while (resultado.next()) {
                System.out.println(resultado.getString(1));
                filastotales = Integer.valueOf(resultado.getString(1));
            }
            resultado = sentencia.executeQuery("SELECT * FROM persona");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error classnotfound " + ex);
        } catch (SQLException ex) {
            System.out.println("Error al hacer la conexion " + ex);
        }
    }

    public static void asign() {
        int filaact = 0;
        System.out.println("imprimiendo");
        MyTablemod dm = (MyTablemod) tbl_employees.getModel();
        try {
            while (resultado.next()) {
                Object[] fila = new Object[5];
                for (int i = 0; i < 5; i++) {
                    fila[i] = resultado.getObject(i + 1);
                }
                dm.setValueAt(fila, filaact);
                filaact++;
            }
        } catch (SQLException sq) {
            System.out.println("Error print query" + sq);
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar " + ex);
            }

        }
    }
    
    public static void insert() {
        try {
            sentencia.executeUpdate("UPDATE persona SET sexo='" + txt_sexo.getText() + "' WHERE nombre='" + txt_nombre.getText() + "';");
            System.out.println("Sexo cambiado correctamente");
        } catch (Exception ex) {
            System.out.println("Error al ejecutar sentencia SQL " + ex);
            System.out.println("Error al cambiar el sexo");
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (sentencia != null) {
                    sentencia.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar " + ex);
            }
        }
    }

    public static void conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/EJEMPLO", "root", "sas588585");
            sentencia = conexion.createStatement();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error classnotfound " + ex);
        } catch (SQLException ex) {
            System.out.println("Error al hacer la conexion " + ex);
        }
        System.out.println("Conectado");
        insert();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btn_modificar) {
            conexion();
        }
    }
}
