package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.litvak.userservice.model.entity.EnumLocalization;

import java.util.List;

public interface EnumLocalizationRepository extends JpaRepository<EnumLocalization, Long> {
    List<EnumLocalization> findByEnumTypeAndLocale(String enumType, String locale);
}
