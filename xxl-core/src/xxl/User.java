package xxl;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an user of the Calculator.
 */
public class User implements Serializable {
    /** User name */
    private String _name;

    /** List of Spreadsheet's filenames */
    private List<String> _spreadsheets;

    /**
     * Constructor.
     * @param name of the user
     */
    public User(String name) {
        _name = name;
    }

    public void addSpreadsheet(String filename) {
        _spreadsheets.add(filename);
    }

    public void removeSpreadsheet(String filename) {
        _spreadsheets.remove(filename);
    }

    public String getName() {
        return _name;
    }
}
