package csye6225Web.repositories;


import csye6225Web.models.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SensorDataRepository extends JpaRepository<SensorData,Long>
{


}
