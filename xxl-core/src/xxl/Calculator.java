package xxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.user.User;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** Name of current user */
    private String _user = "root";

    /** Store for users */
    private HashMap<String, User> _users = new HashMap<>(1);

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the Spreadsheet does not have a name.
     * @throws IOException if there is some error while serializing the spreadsheet to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_spreadsheet.getFilename().isBlank()) throw new MissingFileAssociationException();
        _spreadsheet.clean();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_spreadsheet.getFilename()))) {
            out.writeObject(getSpreadsheet());
        }
    }

    /**
     * Saves the serialized spreadsheet into the specified file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the Spreadsheet does not have a name.
     * @throws IOException if there is some error while serializing the spreadsheet to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _spreadsheet.setFilename(filename);
        save();
    }

    /**
     * Loads a spreadsheet file and opens it.
     * 
     * @param filename name of the file containing the serialized spreadsheet to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        if (filename == null || filename.equals("")) throw new UnavailableFileException(filename);

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            _spreadsheet = (Spreadsheet) in.readObject();
            _spreadsheet.setFilename(filename);
            loadUsers();
            _spreadsheet.addUser(getUser());
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            /* Read line and column */
            int lines = Integer.parseInt(in.readLine().split("=")[1]);
            int column = Integer.parseInt(in.readLine().split("=")[1]);
            newSpreadsheet(lines, column);
            /* Read each line in a loop */
            String s;
            while ((s = in.readLine()) != null) {
                String[] line = new String(s.getBytes(), "UTF-8").split("\\|");
                /* Ignore empty cells */
                if (line.length != 2) continue;
                _spreadsheet.insertContents(line[0], line[1]);
            }
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
    }

    /**
     * Getter for the Spreadsheet.
     * 
     * @return current opened spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }

    /**
     * Creates a new spreadsheet with the given number of lines and columns.
     * 
     * @param lines
     * @param columns
     */
    public void newSpreadsheet(int lines, int columns) {
        _spreadsheet = new Spreadsheet(lines, columns);
        _spreadsheet.addUser(getUser());
    }

    /**
     * Reads users in spreadsheet and adds them to the Calculator 
     * if they do not exist already, otherwise overwrites them in 
     * the Spreadsheet.
     */
    protected void loadUsers() {
        for (User user : _spreadsheet.users())
            if (_users.containsKey(user.getName()))
                _spreadsheet.addUser(user);
            else
                _users.put(user.getName(), user);
    }

    /**
     * Will create the current User if it does not exist.
     * 
     * @return the current User.
     */
    public User getUser() {
        if (_users.get(_user) == null) 
            _users.put(_user, new User(_user));
        return _users.get(_user);
    }

    /**
     * @return true if there are unsaved changes on the spreadsheet.
     */
    public boolean isDirty() {
        return _spreadsheet != null && _spreadsheet.isDirty();
    }
}
