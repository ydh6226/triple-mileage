create index point_event_index_place_Id_and_review_id_and_mileage
    on point_event (place_id, review_id, mileage);

create index point_event_index_place_Id_and_user_id_and_create_date
    on point_event (place_id, user_id, created_date);

create index point_event_review_id
    on point_event (review_id);
