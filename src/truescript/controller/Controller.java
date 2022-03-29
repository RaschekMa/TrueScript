package truescript.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import truescript.classes.Generator;
import truescript.classes.Statement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Controller
{
    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane pane_archive, pane_calc, pane_options, pane_pruef;

    @FXML
    private Button btn_archive, btn_calc, btn_options, btn_pruef;

    @FXML
    private TextArea mainConsole, resultArea, matrixArea;

    @FXML
    private TextField mainInput;

    @FXML
    private CheckBox checkBoxOR, checkBoxAND, checkBoxXNOR, checkBoxIMP;

    @FXML
    private ChoiceBox<Integer> count;

    @FXML
    private ChoiceBox<String> length;

    private Generator gen = new Generator();
    private Statement a;
    private File filepath;

    //-------------------------------------------Initialize-------------------------------------------------------------

    @FXML
    public void initialize()
    {
        initCount();
        initLength();
    }

    public void initCount()
    {
        count.getItems().add(2);
        count.getItems().add(3);
        count.getItems().add(4);
        count.getItems().add(5);
        count.getItems().add(6);

        count.setValue(3);
    }

    public void initLength()
    {
        length.getItems().add("kurz");
        length.getItems().add("mittel");
        length.getItems().add("lang");

        length.setValue("mittel");
    }

    //-------------------------------------------onClickMethods----------------------------------------------------------------

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        if(event.getSource() == btn_archive)
        {
            pane_archive.toFront();
        }
        else if(event.getSource() == btn_calc)
        {
            pane_calc.toFront();
        }
        else if(event.getSource() == btn_pruef)
        {
            pane_pruef.toFront();
        }
        else if(event.getSource() == btn_options)
        {
            pane_options.toFront();
        }
    }

    public void onClickExit(MouseEvent mouseEvent)
    {
        System.exit(0);
    }

    public void onClickClear(MouseEvent mouseEvent)
    {
        mainConsole.setText("");
        matrixArea.setText("");
        resultArea.setText("");
    }

    public void onClickClearInput(MouseEvent mouseEvent)
    {
        mainInput.setText("");
    }

    public void onClickClearLastInput(MouseEvent mouseEvent)
    {
        mainInput.setText(removeLastChar(mainInput.getText()));
    }

    public void onClickOutput(MouseEvent mouseEvent)
    {
        a = new Statement(mainInput.getText());
        mainConsole.setText(mainConsole.getText() + a.toString());
        matrixArea.setText(a.toStringMatrix());
        resultArea.setText(a.toStringResult());
    }

    public void onClickGenerate(MouseEvent mouseEvent)
    {
        mainInput.setText((gen.generate(getLength(), getCount(), checkBoxOR.isSelected(), checkBoxAND.isSelected(), checkBoxXNOR.isSelected(), checkBoxIMP.isSelected())));
        mainConsole.setText(mainConsole.getText() + "The statement \"" + mainInput.getText() + "\" was generated.\n\n");
    }

    public void onClickGetValue(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String str = inputSwapper(button.getText());
        mainInput.setText(mainInput.getText() + str);
    }

    public void onClickOpenFileChooser(MouseEvent mouseEvent)
    {
        FileChooser file = new FileChooser();
        file.setTitle("Eine Datei aussuchen");
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        filepath = file.showSaveDialog(new Stage());
    }

    public void onClickSaved(MouseEvent mouseEvent) throws IOException
    {
      if(filepath != null) {
          FileWriter print = new FileWriter(filepath, true);
          print.write(mainInput.getText() + " (gespeichert an: " + LocalDate.now() + ")" + "\n");
          print.close();
      }
    }

    //----------------------------------------MiscMethods---------------------------------------------------------------

    public int getCount()
    {
        return count.getValue();
    }

    public String getLength()
    {
        return length.getValue();
    }

    public String inputSwapper(String input)
    {
        return switch (input) {
            case "OR" -> "∨";
            case "AND" -> "∧";
            case "XNOR" -> "↔";
            case "IMP" -> "→";
            case "NOT" -> "¬";
            case "(" -> "(";
            case ")" -> ")";
            default -> input;
        };
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str);
    }

    public static String removeLastChars(String str)
    {
        if(str.length() > 0)
        {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void createGrid(int a, int b)
    {
        for(int i = 0; i < a; i++)
        {
            for(int j = 0; j < b; j++)
            {
                grid.add(new Label(), i, j);
            }
        }
    }
}
