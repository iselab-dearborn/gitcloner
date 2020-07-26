package edu.iselab.cloner.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.table.DefaultTableModel;

import edu.iselab.cloner.model.Project;
import lombok.Getter;

@Getter
public class DataModel extends DefaultTableModel {

    private static final long serialVersionUID = 1090429482048205231L;
    
    protected String[] columnNames = { "Project", "Status" };
    
    protected Map<Project, String> progress = new ConcurrentHashMap<>();
    
    public DataModel(List<Project> projects) {

        for (Project project : projects) {
            progress.put(project, "Pending");
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {

        if (progress == null) {
            return 0;
        }

        return this.progress.size();
    }
    
    @Override
    public boolean isCellEditable(int row, int column){  
        return false;  
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int column) {

        List<Project> p = new ArrayList<>(progress.keySet());
        
        Project project = p.get(row);

        if (column == 0) {
            return project.getDirectory();
        }

        return progress.get(project);
    }
}
