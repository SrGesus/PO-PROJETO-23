package xxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import xxl.exceptions.FunctionNameException;
import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.user.DataStore;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** The filename of the current spreadsheet */
    private String _filename = null;

    /** Current user */
    private String _user = "root";

    /** Store for user-spreadsheet relationship */
    private DataStore _dataStore = new DataStore(_user);

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_filename == null) throw new MissingFileAssociationException();
        _spreadsheet.clean();
        _dataStore.addSpreadsheet(_filename, _user);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_filename))) {
            out.writeObject(getSpreadsheet());
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        if (filename == null || filename.equals("")) throw new UnavailableFileException(filename);

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            _spreadsheet = (Spreadsheet) in.readObject();
            _filename = filename;
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
        _filename = null;
        _dataStore.addSpreadsheet(_filename, _user);
    }

    /**
     * @return true if there are unsaved changes on the spreadsheet.
     */
    public boolean isDirty() {
        return _spreadsheet != null && _spreadsheet.isDirty();
    }
}
