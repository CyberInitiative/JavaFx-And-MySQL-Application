package mysqlandjavafx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElectricGuitar {

    private SimpleIntegerProperty guitarId;
    private SimpleIntegerProperty stringsNumber;
    private SimpleStringProperty bodyShape;
    private SimpleDoubleProperty scale;
    private SimpleStringProperty color;
    private SimpleStringProperty model;
    private SimpleIntegerProperty manufacturerId;

    public ElectricGuitar() {
    }

    public ElectricGuitar(Integer guitarId, Integer stringsNumber,
            String bodyShape, Double scale, String color, String model, Integer manufacturerId) {
        this.guitarId = new SimpleIntegerProperty(guitarId);
        this.stringsNumber = new SimpleIntegerProperty(stringsNumber);
        this.bodyShape = new SimpleStringProperty(bodyShape);
        this.scale = new SimpleDoubleProperty(scale);
        this.color = new SimpleStringProperty(color);
        this.model = new SimpleStringProperty(model);
        this.manufacturerId = new SimpleIntegerProperty(manufacturerId);
    }

    public IntegerProperty getGuitarId() {
        return guitarId;
    }

    public void setGuitarId(int guitarId) {
        this.guitarId.set(guitarId);
    }

    public IntegerProperty getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId.set(manufacturerId);
    }

    public IntegerProperty getStringsNumber() {
        return stringsNumber;
    }

    public void setStringsNumber(int stringsNumber) {
        this.stringsNumber.set(stringsNumber);
    }

    public StringProperty getBodyShape() {
        return bodyShape;
    }

    public void setBodyShape(String bodyShape) {
        this.bodyShape.set(bodyShape);
    }

    public DoubleProperty getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public StringProperty getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public StringProperty getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public Integer getFromGuitarIdProp() {
        return this.guitarId.get();
    }

    public Integer getFromManufacturerIdProp() {
        return this.manufacturerId.get();
    }

    public Integer getFromStringsNumberProp() {
        return this.stringsNumber.get();
    }

    public String getFromBodyShapeProp() {
        return this.bodyShape.get();
    }

    public Double getFromScaleProp() {
        return this.scale.get();
    }

    public String getFromColorProp() {
        return this.color.get();
    }

    public String getFromModelProp() {
        return this.model.get();
    }
}
