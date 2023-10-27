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
    private Set<String> _spreadsheets = new HashSet<>(1);

    /**
     * Constructor.
     * @param name of the user
     */
    public User(String name) {
        _name = name;
    }

    Set<String> getSpreadsheets() {
        return _spreadsheets;
    }

    /**
     * Add a spreadsheet to this User.
     * @param filename
     */
    public void addSpreadsheet(String filename) {
        _spreadsheets.add(filename);
    }

    /**
     * Remove spreadsheet from this User.
     * @param filename
     */
    public void removeSpreadsheet(String filename) {
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

    /** @see Object#equals(Object) */
    @Override
    public boolean equals(Object o) {
        if (o instanceof User user) {
            return getName().equals(user.getName());
        }
        return false;
    }
}
