package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.exceptions.InvalidRangeException;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        // FIXME implement command
        try {
            _display.popup(_receiver.showGama(stringField("gama")));
        } catch (InvalidRangeException e) {
            throw new InvalidCellRangeException(e.getExpression());
        }
    }

}
