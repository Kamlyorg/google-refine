package com.metaweb.gridworks.expr;

import java.util.Properties; 

import com.metaweb.gridworks.model.Cell;
import com.metaweb.gridworks.model.Project;
import com.metaweb.gridworks.model.Row;

public class ExpressionUtils {
    static public Properties createBindings(Project project) {
        Properties bindings = new Properties();
        
        bindings.put("true", true);
        bindings.put("false", false);
        
        bindings.put("project", project);
        
        return bindings;
    }
    
    static public void bind(Properties bindings, Row row, int rowIndex, Cell cell) {
        bindings.put("row", row);
        bindings.put("rowIndex", rowIndex);
        bindings.put("cells", row.getField("cells", bindings));
        
        if (cell == null) {
            bindings.remove("cell");
            bindings.remove("value");
        } else {
            bindings.put("cell", cell);
            bindings.put("value", cell.value);
        }
    }
    
    static public boolean isBlank(Object o) {
        return o == null || (o instanceof String && ((String) o).length() == 0);
    }

    static public boolean isTrue(Object o) {
        return o != null && 
            (o instanceof Boolean ? 
                ((Boolean) o).booleanValue() : 
                Boolean.parseBoolean(o.toString()));
    }
}
