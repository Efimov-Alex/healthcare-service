import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;

public class TestTemperature {
    @Test
    void test_check_temperature_message_out() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        BloodPressure currentPressure = new BloodPressure(60, 120);

        File repoFile = new File("patients_new.txt");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

        Mockito.when(patientInfoRepository.getById(anyString())).
                thenReturn(new PatientInfo("Никита", "Филатов", LocalDate.of(2020, 11, 26),
                        new HealthInfo(new BigDecimal("39.0"), new BloodPressure(120, 80)))
                );


        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = Mockito.mock(MedicalServiceImpl.class);


        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);


        BigDecimal currentTemperature = new BigDecimal("37.0");
        medicalService.checkTemperature("22be8312-e476-4fea-a2ca-7cd19a841162", currentTemperature);


        Mockito.verify(sendAlertService, Mockito.times(1)).
                send("Warning, patient with id: null, need help");
    }

    @Test
    void test_check_temperature_message_not_out() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        BloodPressure currentPressure = new BloodPressure(60, 120);

        File repoFile = new File("patients_new.txt");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

        Mockito.when(patientInfoRepository.getById(anyString())).
                thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
                );


        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = Mockito.mock(MedicalServiceImpl.class);


        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);


        BigDecimal currentTemperature = new BigDecimal("37.0");
        medicalService.checkTemperature("76bcdb91-5cbc-4b7e-825c-5aac5383d160", currentTemperature);


        Mockito.verify(sendAlertService, Mockito.times(0)).
                send("Warning, patient with id: null, need help");
    }
}
