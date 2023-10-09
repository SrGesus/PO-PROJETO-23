package xxl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DataStore implements Serializable {

    /** Name->Users */
    private HashMap<String, User> _users = new HashMap<>(1);

    /** Spreadsheet->Users */
    private HashMap<String, Collection<User>> _spreadsheetUsers = new HashMap<>();

    /**
     * Constructor.
     * @param defaultUser
     */
    public DataStore(String defaultUser) {
        _users.put(defaultUser, new User(defaultUser));
    }

    /**
     * 
     * @param filename
     */
    public void addSpreadsheet(String filename, String user) {
        Collection<User> spreadsheet = _spreadsheetUsers.get(filename);
        if (spreadsheet == null) _spreadsheetUsers.put(filename, new ArrayList<User>());
        
    }


    public void renameSpreadsheet(String oldFilename, String newFilename) {
        for (User user : _spreadsheetUsers.get(oldFilename)) {
            user.removeSpreadsheet(oldFilename);
            user.addSpreadsheet(newFilename);
        }
    }
}
