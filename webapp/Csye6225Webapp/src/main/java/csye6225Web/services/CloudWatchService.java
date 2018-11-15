package csye6225Web.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class CloudWatchService {


    public void putMetricData(String dimensionValue, String metricName, Double data_points)
    {
        ResourcePropertySource propertySource2=null;
        try   { propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");}
        catch (IOException e){ System.out.println(e.getMessage()); }

        String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();

        AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        AmazonCloudWatch cw = AmazonCloudWatchClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        Dimension dimension = new Dimension()
                .withName("API_Request_Count")
                .withValue(dimensionValue);

        MetricDatum datum=new MetricDatum().withMetricName(metricName).withUnit(StandardUnit.None).withValue(data_points).withDimensions(dimension);

        try {
            PutMetricDataRequest request = new PutMetricDataRequest().withNamespace("Csye6225WebApp").withMetricData(datum);
            PutMetricDataResult response = cw.putMetricData(request);
            System.out.println("Successfully updated new metrics data, HttpStatusCode:"+response.getSdkHttpMetadata().getHttpStatusCode());
        }catch (Exception e)
        {
            System.out.println(e.getMessage()+"Could not update new MetricData");
        }

    }



}
