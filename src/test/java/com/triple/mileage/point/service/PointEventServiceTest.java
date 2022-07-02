package com.triple.mileage.point.service;

import com.triple.mileage.common.exception.ErrorCode;
import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointException;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.repository.PointEventRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.rule.PointAdditionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointEventServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PLACE_ID = UUID.randomUUID();
    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final String CONTENT = "내용";

    private static final PointEvent ADD_CONTENT_EVENT = new PointEvent(REVIEW_ID, Reason.ADD_CONTENT, USER_ID, PLACE_ID);
    private static final PointEvent ADD_FIRST_REVIEW_EVENT = new PointEvent(REVIEW_ID, Reason.ADD_FIRST_REVIEW, USER_ID, PLACE_ID);

    @InjectMocks
    PointEventService eventService;

    @Mock
    PointEventRepository eventRepository;

    @Spy
    List<PointAdditionRule> rules = new ArrayList<>();

    @Captor
    ArgumentCaptor<List<PointEvent>> captor;

    @BeforeEach
    void initRules() {
        rules.add(new SpyPhotoPointRule());
        rules.add(new SpyContentPointRule());
        rules.add(new SpyPlacePointRule());
    }

    @DisplayName("해당 장소에 유저가 작성한 유효한 리뷰가 이미 있는 경우 예외를 던진다.")
    @Test
    void add_alreadyExistsActiveReview_throwException() {
        //given
        // 이미 작성된 리뷰 있음
        given(eventRepository.findByPlaceIdAndUserIdOrderByCreatedDate(PLACE_ID, USER_ID))
                .willReturn(singletonList(new PointEvent(UUID.randomUUID(), Reason.ATTACH_PHOTO, USER_ID, PLACE_ID)));

        PointAdditionCommand command = new PointAdditionCommand(USER_ID, Collections.emptyList(), PLACE_ID, CONTENT, UUID.randomUUID());

        //expect
        assertThatExceptionOfType(PointException.class)
                .isThrownBy(() -> eventService.add(command))
                .matches(e -> e.getErrorCode() == ErrorCode.ALREADY_WRITTEN_REVIEW);
    }

    @DisplayName("리뷰 작성으로 인해 포인트가 적립되는 경우")
    @Test
    void add() {
        //given
        // 작성된 리뷰 없음.
        given(eventRepository.findByPlaceIdAndUserIdOrderByCreatedDate(PLACE_ID, USER_ID))
                .willReturn(emptyList());

        PointAdditionCommand command = new PointAdditionCommand(USER_ID, Collections.emptyList(), PLACE_ID, CONTENT, UUID.randomUUID());

        //when
        int additionalPoint = eventService.add(command);

        //then
        assertThat(additionalPoint).isEqualTo(2); // 텍스트 1점, 첫 리뷰 1점

        verify(eventRepository).saveAll(captor.capture());
        List<PointEvent> savedEvents = captor.getValue();
        assertThat(savedEvents).containsExactlyInAnyOrder(ADD_CONTENT_EVENT, ADD_FIRST_REVIEW_EVENT);

    }

    @DisplayName("포인트 회수")
    @Test
    void withdraw() {
        // given
        given(eventRepository.findByReviewIdOrderByCreatedDate(REVIEW_ID))
                .willReturn(List.of(
                        // 기존에 생성된 이벤트들
                        new PointEvent(REVIEW_ID, Reason.ADD_CONTENT, USER_ID, PLACE_ID),
                        new PointEvent(REVIEW_ID, Reason.ATTACH_PHOTO, USER_ID, PLACE_ID)
                ));

        // when
        int changedPoint = eventService.withdraw(REVIEW_ID);

        // then
        assertThat(changedPoint).isEqualTo(Reason.DEL_CONTENT.getPoint() + Reason.DETACH_PHOTO.getPoint());

        verify(eventRepository).saveAll(captor.capture());
        List<PointEvent> withdrawEvents = captor.getValue();

        assertThat(withdrawEvents).containsExactlyInAnyOrder(
                new PointEvent(REVIEW_ID, Reason.DEL_CONTENT, USER_ID, PLACE_ID),
                new PointEvent(REVIEW_ID, Reason.DETACH_PHOTO, USER_ID, PLACE_ID)
        );
    }

    @DisplayName("해당 리뷰에 회수할 포인트가 없다면 새로운 이벤트를 생성하지 않는다.")
    @Test
    void withdraw2() {
        // given
        given(eventRepository.findByReviewIdOrderByCreatedDate(REVIEW_ID))
                .willReturn(List.of(
                        // 해당 리뷰는 이미 회수 처리가 된 상태
                        new PointEvent(REVIEW_ID, Reason.ADD_CONTENT, USER_ID, PLACE_ID),
                        new PointEvent(REVIEW_ID, Reason.ATTACH_PHOTO, USER_ID, PLACE_ID),
                        new PointEvent(REVIEW_ID, Reason.DEL_CONTENT, USER_ID, PLACE_ID),
                        new PointEvent(REVIEW_ID, Reason.DETACH_PHOTO, USER_ID, PLACE_ID)
                ));

        // when
        int changedPoint = eventService.withdraw(REVIEW_ID);

        // then
        assertThat(changedPoint).isZero();

        verify(eventRepository).saveAll(captor.capture());
        List<PointEvent> withdrawEvents = captor.getValue();

        assertThat(withdrawEvents).isEmpty();
    }

    // 사진으로 포인트를 얻을 수 없다고 가정
    private static class SpyPhotoPointRule implements PointAdditionRule {
        @Override
        public boolean isCreatable(PointAdditionCommand command) {
            return false;
        }
        @Override
        public PointEvent create(PointAdditionCommand command) {
            return null;
        }
    }

    // 텍스트로 포인트를 얻을 수 있다고 가정
    private static class SpyContentPointRule implements PointAdditionRule {
        @Override
        public boolean isCreatable(PointAdditionCommand command) {
            return true;
        }
        @Override
        public PointEvent create(PointAdditionCommand command) {
            return ADD_CONTENT_EVENT;
        }
    }

    // 장소로 포인트를 얻을 수 있다고 가정
    private static class SpyPlacePointRule implements PointAdditionRule {
        @Override
        public boolean isCreatable(PointAdditionCommand command) {
            return true;
        }
        @Override
        public PointEvent create(PointAdditionCommand command) {
            return ADD_FIRST_REVIEW_EVENT;
        }
    }
}
