package com.fanhab.portal.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GracefulError {

    Integer statusCode;
    String statusMessage;
    String gracefulMessage;
    String requestPath;
    LocalDateTime occurrenceTime;

}
