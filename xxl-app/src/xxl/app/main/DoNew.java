package xxl.app.main;

import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.exceptions.MissingFileAssociationException;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
        addIntegerField("lines", Prompt.lines());
        addIntegerField("columns", Prompt.columns());
    }

    @Override
    protected final void execute() throws CommandException {
        if (_receiver.getSpreadsheet() != null &&
            Form.confirm(Prompt.saveBeforeExit())) {
            try {
                _receiver.save();
            } catch (IOException e) {
                throw new FileOpenFailedException(e);
            } catch (MissingFileAssociationException e) {
                String filename = Form.requestString(Prompt.saveAs());
                try {
                    _receiver.saveAs(filename);
                } catch (IOException e2) {
                    throw new FileOpenFailedException(e2);
                } catch (MissingFileAssociationException e2) {
                    /** Unreachable */
                    throw new FileOpenFailedException(e2);
                }
            }
        }
        _receiver.newSpreadsheet(integerField("lines"), integerField("columns"));
    }

}
