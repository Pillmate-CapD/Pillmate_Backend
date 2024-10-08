package pillmate.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pillmate.backend.common.exception.NotFoundException;
import pillmate.backend.common.exception.errorcode.ErrorCode;
import pillmate.backend.dto.diary.CreateDiaryRequest;
import pillmate.backend.dto.diary.CreateDiaryResponse;
import pillmate.backend.dto.diary.EditDiaryRequest;
import pillmate.backend.dto.diary.MonthlyScore;
import pillmate.backend.dto.diary.ShowDiaryResponse;
import pillmate.backend.dto.diary.Today;
import pillmate.backend.entity.Diary;
import pillmate.backend.entity.member.Member;
import pillmate.backend.repository.DiaryRepository;
import pillmate.backend.repository.MemberRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pillmate.backend.common.exception.errorcode.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
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

    public Today show(Long memberId, LocalDate date) {
        Diary diary = diaryRepository.findByMemberIdAndAndDate(memberId, date);
        return Today.builder().symptoms(diary.getSymptom())
                              .record(diary.getRecord())
                              .score(diary.getScore())
                              .comment(findByScore(diary.getScore()))
                              .build();
    }

    public List<MonthlyScore> showMonthlyScore(Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        List<Diary> diaries = diaryRepository.findDiariesByMemberIdAndDateRange(memberId, firstDayOfMonth, today);

        return diaries.stream()
                .map(diary -> MonthlyScore.builder()
                          .date(diary.getDate())
                          .score(diary.getScore())
                          .build())
                .sorted(Comparator.comparing(MonthlyScore::getDate))
                .collect(Collectors.toList());
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
