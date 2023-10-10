package xxl.app.main;

import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.exceptions.MissingFileAssociationException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.save();
        } catch (MissingFileAssociationException e) {
            String filename = Form.requestString(Prompt.newSaveAs());
            try {
                _receiver.saveAs(filename);
            } catch (IOException e2) {
                throw new FileOpenFailedException(e2);
            } catch (MissingFileAssociationException e2) {
                /** Unreachable */
                // throw new FileOpenFailedException(e2);
            }
        } catch (IOException e) {
            throw new FileOpenFailedException(e);
        }
    }

}
