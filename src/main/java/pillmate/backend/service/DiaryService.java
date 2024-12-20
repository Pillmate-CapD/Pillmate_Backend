package pillmate.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pillmate.backend.common.exception.NotFoundException;
import pillmate.backend.dto.alarm.SimpleAlarmInfo;
import pillmate.backend.dto.diary.CreateDiaryRequest;
import pillmate.backend.dto.diary.CreateDiaryResponse;
import pillmate.backend.dto.diary.EditDiaryRequest;
import pillmate.backend.dto.diary.PainInfo;
import pillmate.backend.dto.diary.ShowDiaryResponse;
import pillmate.backend.dto.diary.TotalInfo;
import pillmate.backend.dto.diary.Today;
import pillmate.backend.entity.Alarm;
import pillmate.backend.entity.Diary;
import pillmate.backend.entity.MedicinePerMember;
import pillmate.backend.entity.member.Member;
import pillmate.backend.entity.member.Symptom;
import pillmate.backend.repository.AlarmRepository;
import pillmate.backend.repository.DiaryRepository;
import pillmate.backend.repository.MedicinePerMemberRepository;
import pillmate.backend.repository.MemberRepository;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

import static pillmate.backend.common.exception.errorcode.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {
    private final AlarmRepository alarmRepository;
    private final DiaryRepository diaryRepository;
    private final MedicinePerMemberRepository medicinePerMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateDiaryResponse create(Long memberId, CreateDiaryRequest createDiaryRequest) {
        Diary diary = diaryRepository.save(createDiaryRequest.toEntity(findByMemberId(memberId)));
        return CreateDiaryResponse.builder().diaryId(diary.getId()).build();
    }

    @Transactional
    public void edit(Long diaryId, EditDiaryRequest editDiaryRequest) {
        Diary diary = findById(diaryId);
        diary.update(editDiaryRequest.getSymptom(), editDiaryRequest.getScore(), editDiaryRequest.getRecord());
    }

    public List<Symptom> showSymptoms(Long memberId) {
        return findByMemberId(memberId).getSymptoms();
    }

    public Today show(Long memberId, LocalDate date) {
        List<SimpleAlarmInfo> alarms = findAlarmsByMemberId(memberId).stream()
                .filter(alarm -> alarm.getMedicinePerMember().getCreated().isBefore(date) || alarm.getMedicinePerMember().getCreated().isEqual(date))
                .filter(alarm -> alarm.getMedicinePerMember().getCreated().plusDays(alarm.getMedicinePerMember().getDay()).isAfter(date)
                        || alarm.getMedicinePerMember().getCreated().plusDays(alarm.getMedicinePerMember().getDay()).isEqual(date))
                .map(alarm -> SimpleAlarmInfo.builder()
                        .name(alarm.getMedicinePerMember().getMedicine().getName())
                        .category(alarm.getMedicinePerMember().getMedicine().getCategory())
                        .time(alarm.getTimeSlot().getPickerTime()).build())
                .toList();
        Diary diary = getDiary(memberId, date);

        if (diary == null) {
            return Today.builder().alarms(alarms).build();
        }

        return Today.builder().alarms(alarms)
                .id(diary.getId())
                .symptoms(diary.getSymptom())
                .record(diary.getRecord())
                .score(diary.getScore())
                .comment(findByScore(diary.getScore()))
                .build();
    }

    public ShowDiaryResponse showMonthly(Long memberId, LocalDate date) {
        List<MedicinePerMember> medicines = findMedicineByMemberId(memberId);
        List<TotalInfo> totalInfos = medicines.stream()
                .map(medicinePerMember -> TotalInfo.builder()
                        .name(medicinePerMember.getMedicine().getName())
                        .category(medicinePerMember.getMedicine().getCategory())
                        .startDate(medicinePerMember.getCreated())
                        .endDate(medicinePerMember.getCreated().plusDays(medicinePerMember.getDay()))
                        .build()).toList();
        Long duration = ChronoUnit.DAYS.between(findByMemberId(memberId).getCreated(), LocalDate.now()) + 1;
        return ShowDiaryResponse.builder()
                .duration(duration)
                .painsPerDay(getPainsPerDay(memberId, date))
                .totalInfo(totalInfos)
                .today(show(memberId, date))
                .build();
    }

    private Diary getDiary(Long memberId, LocalDate date) {
        return diaryRepository.findByMemberIdAndAndDate(memberId, date);
    }

    private List<PainInfo> getPainsPerDay(Long memberId, LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        List<Diary> diaries = diaryRepository.findDiariesByMemberIdAndDateRange(memberId, firstDayOfMonth, lastDayOfMonth);
        return diaries.stream().map(diary -> PainInfo.builder().date(diary.getDate()).level(diary.getScore()).build()).toList();
    }

    private List<Alarm> findAlarmsByMemberId(Long memberId) {
        return alarmRepository.findAllByMemberId(memberId);
    }

    private List<MedicinePerMember> findMedicineByMemberId(Long memberId) {
        return medicinePerMemberRepository.findAllByMemberId(memberId);
    }

    private Diary findById(Long diaryId) {
        return diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException(NOT_FOUND_DIARY));
    }

    private Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
    }

    private String findByScore(Integer score) {
        if (score == 1) {
            return "통증이 미미하거나 없어요";
        } else if (score == 2) {
            return "약간의 통증이 있지만 문제는 없어요";
        } else if (score == 3) {
            return "통증이 상당한 편이에요";
        } else if (score == 4) {
            return "통증이 일상생활을 방해해요";
        } else if (score == 5) {
            return "통증때문에 일상생활이 어려워요";
        } else if (score == 6) {
            return "통증 때문에 다른 일을 할 수 없어요";
        } else if (score == 7) {
            return "통증이 상당한 편이에요";
        } else if (score == 8) {
            return "통증이 상당해서 참기가 어려워요";
        } else if (score == 9) {
            return "거의 최대의 통증이에요";
        } else if (score == 10) {
            return "표현할 수 없는 최대의 통증이에요";
        } else {
            return "점수가 잘 못 되었습니다.";
        }
    }
}
