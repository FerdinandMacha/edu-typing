package fero.home.vojta.typing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.apache.logging.log4j.*;


public class TypingApp 
{
    private static final Logger logger = LogManager.getLogger(TypingApp.class);

    public static void main( String[] args )
    {
    	logger.trace("Initializing the Display...");
        Display display = new Display();

        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(3, false));

        Table table;
        final Text text = new Text(shell, SWT.BORDER);
    	text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

    	final Spinner countText = new Spinner (shell, SWT.BORDER);
    	countText.setMinimum(0);
    	countText.setMaximum(100);
    	countText.setSelection(50);
    	countText.setIncrement(1);
    	countText.setPageIncrement(10);
    	countText.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));

    	Button btnLoad = new Button(shell, SWT.PUSH);
    	btnLoad.setText("Write to table");
    	btnLoad.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));

    	table = new Table(shell, SWT.BORDER);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
    	
    	TableColumn column1 = new TableColumn(table, SWT.LEFT);
    	TableColumn column2 = new TableColumn(table, SWT.LEFT);
    	column1.setText("Order");
    	column2.setText("Name");
    	column1.setWidth(70);
    	column2.setWidth(70);
    	table.setHeaderVisible(true);

    	btnLoad.addSelectionListener(widgetSelectedAdapter( e -> {
    		int newCount = tryParse(countText.getText(), 1);
	    	reloadTable(table, newCount, text.getText());
	    	if (table.getItemCount() < newCount) {
				loadTable(table, newCount, text.getText());
			} else {
				clearTable(table, newCount);
			}
        }));   	

    	shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
        logger.trace("Finished.");
    }

	private static void loadTable(final Table table, final int totalCount, final String typedText) {
		int oldCount = table.getItemCount();
		int newCount = totalCount - oldCount;
		for (int i = 1; i <= newCount; i++) {
        	TableItem item1 = new TableItem(table, SWT.NONE);
        	item1.setText(new String[] { String.valueOf(i+oldCount), typedText });
		}
	}

	private static void reloadTable(final Table table, final int newCount, final String typedText) {
		int reloadCount = (newCount < table.getItemCount()) ? newCount : table.getItemCount();
		for (int i = 0; i < reloadCount; i++) {
        	TableItem currentRow = table.getItem(i);
        	currentRow.setText(1, typedText);
		}
	}

	private static void clearTable(final Table table, final int newCount) {
		table.remove(newCount, table.getItemCount()-1);
	}

    private static int tryParse(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
