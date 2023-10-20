package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Spreadsheet;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidRangeException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.deleteGama(stringField("gama"));
        } catch (InvalidRangeException e) {
            throw new InvalidCellRangeException(e.getExpression());
        }
    }

}
