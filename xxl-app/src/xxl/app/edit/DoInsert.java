package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import xxl.Spreadsheet;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("gama", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.insertGama(stringField("gama"), stringField("content"));
        } catch (InvalidExpressionException e) {
            throw new InvalidCellRangeException(e.getExpression());
        } catch (FunctionNameException e) {
            throw new UnknownFunctionException(e.getFunctionName());
        }
    }

}
