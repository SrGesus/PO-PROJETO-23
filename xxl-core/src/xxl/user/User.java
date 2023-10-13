package xxl.user;

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
    User(String name) {
        _name = name;
    }

    /**
     * Add a spreadsheet to this User.
     * @param filename
     */
    public void addSpreadsheet(String filename) {
        if (filename == null) return;
        _spreadsheets.add(filename);
    }

    /**
     * Remove spreadsheet from this User.
     * @param filename
     */
    public void removeSpreadsheet(String filename) {
        if (filename == null) return;
        _spreadsheets.remove(filename);
    }

    /**
     * Replaces name of a spreadsheet.
     * @param oldFilename
     * @param newFilename
     */
    public void renameSpreadsheet(String oldFilename, String newFilename) {
        removeSpreadsheet(oldFilename);
        addSpreadsheet(newFilename);
    }

    /**
     * @return user's name.
     */
    public String getName() {
        return _name;
    }
}
