package medical.service;

import medical.dto.DoctorDto;
import medical.exception.NotFoundException;
import medical.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;

    public DoctorService(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> getAll() {
        return doctorRepo.findAll()
                .stream()
                .map(d -> new DoctorDto(d.getId(), d.getFullName(), d.getSpecialty()))
                .toList();
    }

    @Transactional(readOnly = true)
    public DoctorDto getById(Long id) {
        var d = doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bác sĩ"));
        return new DoctorDto(d.getId(), d.getFullName(), d.getSpecialty());
    }
}
