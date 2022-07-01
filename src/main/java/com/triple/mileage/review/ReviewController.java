package com.triple.mileage.review;

import com.triple.mileage.common.api.TripleApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewEventHandler reviewEventHandler;

    @PostMapping("/events")
    public TripleApiResponse<Void> consumeReviewEvent(@RequestBody @Valid ReviewEventRequest request) {
        log.info("consume review event: {}", request);

        reviewEventHandler.handle(request);
        return TripleApiResponse.ok();
    }
}
