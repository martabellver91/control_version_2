package persona;

import javax.swing.table.AbstractTableModel;


public class MyTablemod  extends AbstractTableModel {

    String[] columnheaders = {"Nombre", "Primer apellido", "Segundo apellido", "Edad", "Sexo"};
    int filas, contadorpos;
    Object[][] datos;

    public MyTablemod(int rows) {
        filas = rows;
        datos = new Object[rows][5];
    }

    @Override
    public int getRowCount() {
        return datos.length;
    }

    @Override
    public int getColumnCount() {
        return columnheaders.length;
    }
    
    @Override
    public String getColumnName(int col) {
        return columnheaders[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return datos[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();

    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col < 4) {
            return false;
        } else {
            return true;
        }
    }
    
    public void setValueAt(Object[] values, int row) {
        contadorpos = 0;
        for (int i = 0; i < 5; i++) {
            datos[row][i] = values[i];
            contadorpos++;
            fireTableCellUpdated(row, i);
        }
        
    }
}