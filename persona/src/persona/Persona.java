package persona;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public final class Persona extends JFrame implements ActionListener {

    static Connection conexion = null;
    static Statement sentencia = null;
    static ResultSet resultado = null;
    static String nombre, priapellido, segapellido, sexo;
    static int edad, contador, checkusr;
    static JTextField txt_nombre, txt_priapellido, txt_segapellido, txt_sexo, txt_edad;
    static JLabel lbl_nombre, lbl_priapellido, lbl_segapellido, lbl_sexo, lbl_edad, lbl_spacer, lbl_spacer1, lbl_output;
    static JButton btn_insert, btn_clear;
    /*1*/
    static JButton btn_changesex;
    /*2*/
    static JButton btn_verpersonas;

    public Persona() {
        nombre = "";
        priapellido = "";
        segapellido = "";
        sexo = "";
        edad = 0;
        init();
        panel();
        setTitle("Persona");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
    }

    public void init() {
        txt_nombre = new JTextField();
        txt_priapellido = new JTextField();
        txt_segapellido = new JTextField();
        txt_sexo = new JTextField();
        txt_edad = new JTextField();
        lbl_nombre = new JLabel("Nombre");
        lbl_priapellido = new JLabel("Primer apellido");
        lbl_segapellido = new JLabel("Segundo apellido");
        lbl_sexo = new JLabel("Sexo");
        lbl_edad = new JLabel("Edad");
        lbl_spacer = new JLabel("    ");
        lbl_spacer1 = new JLabel("    ");
        lbl_output = new JLabel("Output: ");
        btn_insert = new JButton("Insert");
        btn_insert.addActionListener(this);
        btn_clear = new JButton("Clear");
        btn_clear.addActionListener(this);
        /*1*/ btn_changesex = new JButton("Cambiar sexo");
        /*1*/ btn_changesex.addActionListener(this);
        /*2*/ btn_verpersonas = new JButton("Listado");
        /*2*/ btn_verpersonas.addActionListener(this);

    }

    public void panel() {
        Panel panel1 = new Panel();
        panel1.setLayout(new GridLayout(6, 2, 2, 2));
        panel1.add(lbl_spacer);
        panel1.add(lbl_spacer1);
        panel1.add(lbl_nombre);
        panel1.add(txt_nombre);
        panel1.add(lbl_priapellido);
        panel1.add(txt_priapellido);
        panel1.add(lbl_segapellido);
        panel1.add(txt_segapellido);
        panel1.add(lbl_sexo);
        panel1.add(txt_sexo);
        panel1.add(lbl_edad);
        panel1.add(txt_edad);
        Panel panel2 = new Panel();
        panel2.add(btn_insert);
        panel2.add(btn_clear);
        /*1*/ panel2.add(btn_changesex);
        /*2*/ panel2.add(btn_verpersonas);
        Panel panel3 = new Panel();
        panel3.add(lbl_output);
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);

    }

    public static void conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/EJEMPLO", "root", "");
            sentencia = conexion.createStatement();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error classnotfound " + ex);
        } catch (SQLException ex) {
            System.out.println("Error al hacer la conexion " + ex);
        }
    }

    /*1*/
    public static void checkusr(String nombre) {
        conexion();
        try {
            sentencia.executeUpdate("SELECT nomnbre FROM persona WHERE nombre='" + nombre + "';");
            while (resultado.next()) {
                if (resultado.getString(1).equals(String.valueOf(0))) {
                    checkusr = 1;
                } else {
                    checkusr = 2;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar sentencia SQL " + ex);
        }
    }/*1*/

    /*1*/

    public static void changesex(String sex, String nombre) {
        checkusr(nombre);
        if (checkusr == 2) {
            try {
                sentencia.executeUpdate("UPDATE persona SET sexo='" + sex + "' WHERE nombre='" + nombre + "';");
                System.out.println("Sexo cambiado correctamente");
                lbl_output.setText(lbl_output.getText() + "Sexo cambiado correctamente");
                lbl_output.setText(lbl_output.getText() + "Datos introducidos correctamente");
            } catch (SQLException ex) {
                System.out.println("Error al ejecutar sentencia SQL " + ex);
                System.out.println("Error al cambiar el sexo");
                lbl_output.setText(lbl_output.getText() + "Error al cambiar el sexo");
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
        } else {
            System.out.println("El usuario no existe");
            lbl_output.setText(lbl_output.getText() + "El usuario no existe");
        }
    }/*1*/

    /*2*/

    public void loadWindow() {
        NewWindow gui = new NewWindow(Persona.this);
        gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        gui.setSize(800, 500);
        gui.setVisible(true);
        setTitle("Grafico");
    }/*2*/


    public static void checkstring(String str, String ref) {
        Pattern p = Pattern.compile("[^a-zA-Z ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        boolean b = m.find();
        if (b) {
            System.out.println(ref + " incorrecto, vuelva a introducirlo");
        } else {
            contador++;
        }
    }

    public static void check() {
        nombre = txt_nombre.getText();
        checkstring(nombre, "Nombre");
        priapellido = txt_priapellido.getText();
        checkstring(priapellido, "Primer apellido");
        segapellido = txt_segapellido.getText();
        checkstring(segapellido, "Segundo apellido");
        sexo = txt_sexo.getText();
        checkstring(sexo, "Sexo");
        try {
            edad = Integer.valueOf(txt_edad.getText());
            contador++;
        } catch (Exception e) {
            System.out.println("Error al comprobar edad " + e);
        }
        insert();
    }

    public static void insert() {
        conexion();
        if (contador == 5) {
            try {
                sentencia.executeUpdate("INSERT INTO persona (nombre, priapellido, segapellido, "
                        + "sexo, edad) VALUES ('" + txt_nombre.getText() + "', '"
                        + txt_priapellido.getText() + "', '" + txt_segapellido.getText() + "', '"
                        + txt_sexo.getText() + "', " + Integer.valueOf(txt_edad.getText()) + ")");
                System.out.println("Datos introducidos correctamente");
                lbl_output.setText(lbl_output.getText() + "Datos introducidos correctamente");
            } catch (SQLException ex) {
                System.out.println("Error al ejecutar sentencia SQL " + ex);
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

        } else {
            System.out.println("No se han introducido los datos");
            lbl_output.setText(lbl_output.getText() + "No se han introducido los datos");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_insert) {
            check();
        }
        if (e.getSource() == btn_clear) {
            txt_nombre.setText("");
            txt_priapellido.setText("");
            txt_segapellido.setText("");
            txt_edad.setText("");
            txt_sexo.setText("");
            lbl_output.setText("Output: ");
            //reset variables
            conexion = null;
            sentencia = null;
            resultado = null;
            nombre = "";
            priapellido = "";
            segapellido = "";
            sexo = "";
            edad = 0;
            contador = 0;
            checkusr = 0;
        }
        /*1*/ if (e.getSource() == btn_changesex) {
            changesex(txt_sexo.getText(), txt_nombre.getText());
        }/*1*/

        /*2*/ if (e.getSource() == btn_verpersonas) {
            loadWindow();
        }/*2*/
    }

    public static void main(String[] args) {
        Persona persona = new Persona();
    }
}
