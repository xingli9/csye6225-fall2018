package csye6225Web.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name="SensorDatas")
public class SensorData {


    @Id
    @GeneratedValue
    private Long id;
    private String startedTime;
    private String timeData;
    private String type;
    private float currValue;
    private float avgValue;
    private float minValue;
    private float maxValue;
    private float totalValue;
    private int count;


    public SensorData()
    {

    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setAvgValue(float avgValue) {
        this.avgValue = avgValue;
    }

    public void setCurrValue(float currValue) {
        this.currValue = currValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public void setStartedTime(String startedTime) {
        this.startedTime = startedTime;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public float getAvgValue() {
        return avgValue;
    }

    public float getCurrValue() {
        return currValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public int getCount() {
        return count;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public String getTimeData() {
        return timeData;
    }

    public String getType() {
        return type;
    }

}
