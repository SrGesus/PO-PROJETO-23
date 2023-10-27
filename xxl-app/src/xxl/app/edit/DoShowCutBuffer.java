package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Spreadsheet;
import xxl.exceptions.InvalidRangeException;


/**
 * Show cut buffer command.
 */
class DoShowCutBuffer extends Command<Spreadsheet> {

    DoShowCutBuffer(Spreadsheet receiver) {
        super(Label.SHOW_CUT_BUFFER, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.showCutBuffer().forEach(str -> _display.popup(str));
        } catch (InvalidRangeException e) {
            throw new InvalidCellRangeException(e.getExpression());
        }
    }
}
