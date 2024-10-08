package pillmate.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pillmate.backend.common.util.LoggedInMember;
import pillmate.backend.dto.medicine.AddRequest;
import pillmate.backend.dto.medicine.MedicineBasicInfo;
import pillmate.backend.dto.medicine.MedicineInfo;
import pillmate.backend.dto.medicine.ModifyMedicineInfo;
import pillmate.backend.dto.medicine.PrescriptionRequest;
import pillmate.backend.dto.medicine.UpcomingAlarm;
import pillmate.backend.service.MedicineService;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medicines")
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping
    public UpcomingAlarm getUpcomingAlarm(@LoggedInMember Long memberId, @RequestParam("time") LocalTime currentTime, @RequestParam("medicineId") Long medicineId) {
        return medicineService.getUpcomingAlarm(memberId, currentTime, medicineId);
    }

    @PostMapping("/name")
    public List<MedicineBasicInfo> getMedicineBasicInfo(@LoggedInMember Long memberId, @RequestBody List<PrescriptionRequest> nameList) {
        return medicineService.getMedicineInfo(memberId, nameList);
    }

    @PostMapping
    public ResponseEntity<String> add(@LoggedInMember Long memberId, @RequestBody AddRequest addRequest) {
        medicineService.add(memberId, addRequest);
        return ResponseEntity.ok("알약 저장이 완료되었습니다.");
    }

    @GetMapping("/all")
    public List<MedicineInfo> showAll(@LoggedInMember Long memberId) {
        return medicineService.showAll(memberId);
    }

    @PatchMapping
    public ResponseEntity<String> modify(@LoggedInMember Long memberId, @RequestBody ModifyMedicineInfo modifyMedicineInfo) {
        medicineService.modify(memberId, modifyMedicineInfo);
        return ResponseEntity.ok("정보 수정이 완료되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@LoggedInMember Long memberId, @RequestParam("medicineId") Long medicineId) {
        medicineService.delete(memberId, medicineId);
        return ResponseEntity.ok("알약 삭제가 완료되었습니다.");
    }
}
