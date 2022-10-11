package mysqlandjavafx;

import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.fxml.FXML;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

/**
 *
 * @author Miroslav Levdikov
 */
public class Controller {

    @FXML
    private TextField manufacturerTextField;
    @FXML
    private TextField stringNumberTextField;
    @FXML
    private TextField bodyShapeTextField;
    @FXML
    private TextField scaleTextField;
    @FXML
    private TextField colorTextField;
    @FXML
    private TextField modelTextField;

    @FXML
    private TableView<ElectricGuitar> tableView;

    private TableColumn<ElectricGuitar, Number> columnGuitarId = new TableColumn("Guitar Id");
    private TableColumn<ElectricGuitar, Number> columnManufacturer = new TableColumn("Manufacturer Id");
    private TableColumn<ElectricGuitar, Number> columnStingsNumber = new TableColumn("Strings Number");
    private TableColumn<ElectricGuitar, String> columnBodyShape = new TableColumn("Body Shape");
    private TableColumn<ElectricGuitar, Number> columnScale = new TableColumn("Scale");
    private TableColumn<ElectricGuitar, String> columnColor = new TableColumn("Color");
    private TableColumn<ElectricGuitar, String> columnModel = new TableColumn("Model");

    private ObservableList<ElectricGuitar> electricGuitarsData;

    private Connection con = Controller.connect("test_database");

    private ElectricGuitar toUpdate;

    @FXML
    private void initialize() {
        tableSettings();
        mySQLConnectionSettings();
        //createSQLTableIfNotExists();
        loadSQLData();

        try {
            con.getMetaData();
        } catch (Exception e) {
            System.out.println("Connection is closed");
        }

    }

    private void tableSettings() {
        electricGuitarsData = FXCollections.<ElectricGuitar>observableArrayList();
        tableView.setItems(electricGuitarsData);
        tableView.getColumns().addAll(columnGuitarId, columnManufacturer, columnStingsNumber, columnBodyShape,
                columnScale, columnColor, columnModel);
        tableView.setRowFactory(tv -> {
            TableRow<ElectricGuitar> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    toUpdate = row.getItem();
                    stringNumberTextField.setText(toUpdate.getFromStringsNumberProp().toString());
                    bodyShapeTextField.setText(toUpdate.getFromBodyShapeProp());
                    scaleTextField.setText(toUpdate.getFromScaleProp().toString());
                    colorTextField.setText(toUpdate.getFromColorProp());
                    modelTextField.setText(toUpdate.getFromModelProp());
                    manufacturerTextField.setText(toUpdate.getFromManufacturerIdProp().toString());
                }
            });
            return row;
        });

        columnGuitarId.setCellValueFactory(cellData -> cellData.getValue().getGuitarId());
        columnStingsNumber.setCellValueFactory(cellData -> cellData.getValue().getStringsNumber());
        columnBodyShape.setCellValueFactory(cellData -> cellData.getValue().getBodyShape());
        columnScale.setCellValueFactory(cellData -> cellData.getValue().getScale());
        columnColor.setCellValueFactory(cellData -> cellData.getValue().getColor());
        columnModel.setCellValueFactory(cellData -> cellData.getValue().getModel());
        columnManufacturer.setCellValueFactory(cellData -> cellData.getValue().getManufacturerId());
    }

    private void loadSQLData() {
        try {
            String SQL = "SELECT * FROM `electric_guitar`";
            ResultSet result = con.createStatement().executeQuery(SQL);
            while (result.next()) {
                ElectricGuitar electricGuitar = new ElectricGuitar(
                        result.getInt("guitar_id"),
                        result.getInt("strings_number"),
                        result.getString("body_shape"),
                        result.getDouble("scale"),
                        result.getString("color"),
                        result.getString("model"),
                        result.getInt("manufacturer_id"));

                electricGuitarsData.add(electricGuitar);
            }
        } catch (SQLException ex) {
            System.out.println("Error on loading data: " + ex.getMessage());
        }
    }

    @FXML
    private void createNewSQLEntry() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO `electric_guitar`"
                    + "(`body_shape`, `strings_number`, `scale`, `color`, `model`, `manufacturer_id`)"
                    + "VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1, bodyShapeTextField.getText());
            preparedStatement.setInt(2, Integer.parseInt(stringNumberTextField.getText()));
            preparedStatement.setDouble(3, Double.parseDouble(scaleTextField.getText()));
            preparedStatement.setString(4, colorTextField.getText());
            preparedStatement.setString(5, modelTextField.getText());
            preparedStatement.setInt(6, Integer.parseInt(manufacturerTextField.getText()));
            preparedStatement.executeUpdate();
            electricGuitarsData.removeAll(electricGuitarsData);
            loadSQLData();
        } catch (SQLException ex) {
            System.out.println("ENTRY CREATION SQL ERROR: " + ex.getMessage());
        }
    }

    @FXML
    private void updateSQLEntry() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE `electric_guitar`"
                    + "SET `manufacturer_id` = ?, `strings_number` = ?, `body_shape` = ?, `scale` = ?, `color` = ?, `model` = ? "
                    + "WHERE `guitar_id` = ? ");
            preparedStatement.setInt(1, Integer.parseInt(manufacturerTextField.getText()));
            preparedStatement.setInt(2, Integer.parseInt(stringNumberTextField.getText()));
            preparedStatement.setString(3, bodyShapeTextField.getText());
            preparedStatement.setDouble(4, Double.parseDouble(scaleTextField.getText()));
            preparedStatement.setString(5, colorTextField.getText());
            preparedStatement.setString(6, modelTextField.getText());
            if (toUpdate != null) {
                preparedStatement.setInt(7, toUpdate.getFromGuitarIdProp());
                preparedStatement.executeUpdate();
                electricGuitarsData.removeAll(electricGuitarsData);
                loadSQLData();
            } else {
                System.out.println("There is nothing to update");
            }
        } catch (SQLException ex) {
            System.out.println("ENTRY CREATION SQL ERROR: " + ex.getMessage());
        }
    }

    @FXML
    private void deleteSQLEntry() {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM `electric_guitar`"
                    + "WHERE `guitar_id` = ? ");
            if (toUpdate != null) {
                preparedStatement.setInt(1, toUpdate.getFromGuitarIdProp());
                preparedStatement.executeUpdate();
                electricGuitarsData.removeAll(electricGuitarsData);
                loadSQLData();
            } else {
                System.out.println("There is nothing to delete");
            }
        } catch (SQLException ex) {
            System.out.println("ENTRY REMOVAL SQL ERROR: " + ex.getMessage());
        }
    }

    private void createSQLTableIfNotExists() {
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `electric_guitar` ("
                    + "`guitar_id` mediumint(9) AUTO_INCREMENT,"
                    + "`manufacturer` CHAR (50) NOT NULL,"
                    + "`strings_number` SMALLINT(3) NOT NULL DEFAULT 6,"
                    + "`body_shape` CHAR(50) NOT NULL,"
                    + "`scale` DOUBLE(4,2) NOT NULL,"
                    + "`color` CHAR(30) NOT NULL,"
                    + " PRIMARY KEY (`guitar_id`) "
                    + ")"
                    + "ENGINE=InnoDB DEFAULT CHARSET=utf8"
            );
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {

        }
//        try {
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            statement = null;
//            con = null;
//        }
    }

    private void mySQLConnectionSettings() {
        System.out.println("Connection successful");
        Statement statement = null;
    }

    public static java.sql.Connection connect(String db) {
        java.sql.Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.0.112:3306/" + db + "?characterEncoding=utf8", "tester",
                    "188506");
        } catch (ClassNotFoundException ex) {
            System.out.println("Драйвер не найден");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
        return con;
    }
}
