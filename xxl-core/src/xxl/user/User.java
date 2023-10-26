package xxl.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an user of the Calculator.
 */
public class User implements Serializable {

    /** User name */
    private String _name;

    /** List of Spreadsheet's filenames */
    private Set<String> _spreadsheets = new HashSet<>();

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
     * @return user's name.
     */
    public String getName() {
        return _name;
    }

    /** @see Object#hashCode() */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
