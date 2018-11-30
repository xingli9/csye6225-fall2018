package csye6225Web.serviceController;

import csye6225Web.models.SensorData;
import csye6225Web.repositories.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class SensorDataController {

    @Autowired
    SensorDataRepository sensorDataRepository;

    @GetMapping("/iotService/SensorData")
    public List<SensorData> getAllSensorData()
    {

        return sensorDataRepository.findAll();

    }


    @PostMapping("/iotService/SensorData")
    public ResponseEntity<Object> createNewTransaction(@RequestBody SensorData sensorData)
    {

        try
        {
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(HttpStatus.CREATED).body("CREATE SUCCESS");

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }



    }
