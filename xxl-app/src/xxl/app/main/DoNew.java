package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;

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
        // FIXME Needs to handle saving older spreadsheetif requested
        if (_receiver.getSpreadsheet() != null &&
            Form.confirm(Prompt.saveBeforeExit())) {
                String s = Form.requestString(Prompt.saveAs());
        }
        _receiver.newSpreadsheet(integerField("lines"), integerField("columns"));
    }

}
