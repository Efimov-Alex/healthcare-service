import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.io.File;
import java.math.BigDecimal;

public class TestTemperature {
    @Test
    void test_checkTemperature() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        File repoFile = new File("patients_new.txt");
        PatientInfoRepository patientInfoRepository = new PatientInfoFileRepository(repoFile, mapper);

        SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);


        BigDecimal currentTemperature = new BigDecimal("37.0");
        medicalService.checkTemperature("2ee734c9-3556-48c9-bdfd-757c979f4d93", currentTemperature);
        medicalService.checkTemperature("76bcdb91-5cbc-4b7e-825c-5aac5383d160", currentTemperature);
        medicalService.checkTemperature("22be8312-e476-4fea-a2ca-7cd19a841162", currentTemperature);


        Mockito.verify(sendAlertService, Mockito.times(0)).
                send("Warning, patient with id: 2ee734c9-3556-48c9-bdfd-757c979f4d93, need help");

        Mockito.verify(sendAlertService, Mockito.times(0)).
                send("Warning, patient with id: 76bcdb91-5cbc-4b7e-825c-5aac5383d160, need help");

        Mockito.verify(sendAlertService, Mockito.times(1)).
                send("Warning, patient with id: 22be8312-e476-4fea-a2ca-7cd19a841162, need help");
    }
}
