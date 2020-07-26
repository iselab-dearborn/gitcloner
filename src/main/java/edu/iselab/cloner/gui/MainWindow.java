package edu.iselab.cloner.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.monitor.GitProgressMonitor;
import edu.iselab.cloner.monitor.ProgressListener;

public class MainWindow extends JFrame{

    protected Color RED = new Color(255,102,102);
    
    protected Color GREEN = new Color(102, 255, 102);
    
    private static final long serialVersionUID = -5936079906175506727L;
    
    public MainWindow(DataModel dataModel) {
        super("Cloner GUI");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JTable table = new JTable(dataModel) {
            
            private static final long serialVersionUID = 9136381199126082845L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                
                Component comp = super.prepareRenderer(renderer, row, col);
                
                String value = (String) getModel().getValueAt(row, col);
                
                comp.setBackground(Color.WHITE);
                 
                if (col == 1 && value.startsWith("Checking out files:      100%")) {
                    comp.setBackground(GREEN);
                }
                
                if (col == 1 && value.startsWith("Exception:")) {
                    comp.setBackground(RED);
                }
                
                if (col == 1 && value.startsWith("It is already cloned")) {
                    comp.setBackground(GREEN);
                }
                
                return comp;
            }
        };
        
        setJTableColumnsWidth(table, 600, 10, 90);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(600, 300);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(3);
//        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        
        this.setPreferredSize(new Dimension(800, 600));
        this.getContentPane().add(scrollPane);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
    public static void createAndShowGUI(List<Project> projects, GitProgressMonitor gitProgressListener) {
        
        DataModel dataModel = new DataModel(projects);
        
        gitProgressListener.add(new ProgressListener() {

            @Override
            public void message(Project project, String message) {
               
                dataModel.getProgress().put(project, message);

                dataModel.fireTableDataChanged();
            }
        });
        
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                
                MainWindow window = new MainWindow(dataModel);
                
                window.setVisible(true);
            }
        });
    }
    
    public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
        
        double total = 0;
        
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }
    }
}
