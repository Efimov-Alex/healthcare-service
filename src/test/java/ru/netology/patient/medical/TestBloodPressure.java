package ru.netology.patient.medical;

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

public class TestBloodPressure {
    @Test
    void test_check_blood_pressure_message_out() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        BloodPressure currentPressure = new BloodPressure(60, 120);

        File repoFile = new File("patients_new.txt");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

        Mockito.when(patientInfoRepository.getById(anyString())).
                thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = Mockito.mock(MedicalServiceImpl.class);


        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure("2ee734c9-3556-48c9-bdfd-757c979f4d93", currentPressure);

        Mockito.verify(sendAlertService, Mockito.times(1)).
                send("Warning, patient with id: null, need help");
    }

    @Test
    void test_check_blood_pressure_message_not_out() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        BloodPressure currentPressure = new BloodPressure(60, 120);

        File repoFile = new File("patients_new.txt");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

        Mockito.when(patientInfoRepository.getById(anyString())).
                thenReturn(new PatientInfo("Артем", "Смирнов", LocalDate.of(2001, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(60, 120))
                ));

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = Mockito.mock(MedicalServiceImpl.class);


        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure("2e5ed0d1-0272-4a55-bf40-d252b4a7ca02", currentPressure);

        Mockito.verify(sendAlertService, Mockito.times(0)).
                send("Warning, patient with id: null, need help");
    }
}
