package com.triple.mileage.point.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Reason {
    ATTACH_PHOTO(Constants.DEFAULT_POINT) {
        @Override
        public Reason getCompensatingReason() {
            return DETACH_PHOTO;
        }
    },
    DETACH_PHOTO(Constants.DEFAULT_COMPENSATING_VALUE) {
        @Override
        public Reason getCompensatingReason() {
            return ATTACH_PHOTO;
        }
    },
    ADD_CONTENT(Constants.DEFAULT_POINT) {
        @Override
        public Reason getCompensatingReason() {
            return DEL_CONTENT;
        }
    },
    DEL_CONTENT(Constants.DEFAULT_COMPENSATING_VALUE) {
        @Override
        public Reason getCompensatingReason() {
            return ADD_CONTENT;
        }
    },
    ADD_FIRST_REVIEW(Constants.DEFAULT_POINT) {
        @Override
        public Reason getCompensatingReason() {
            return DEL_FIRST_REVIEW;
        }
    },
    DEL_FIRST_REVIEW(Constants.DEFAULT_COMPENSATING_VALUE) {
        @Override
        public Reason getCompensatingReason() {
            return ADD_FIRST_REVIEW;
        }
    };

    private static class Constants {
        private static final int DEFAULT_POINT = 1;
        private static final int DEFAULT_COMPENSATING_VALUE = -1 * DEFAULT_POINT;
    }

    private final int point;

    public abstract Reason getCompensatingReason();
}
