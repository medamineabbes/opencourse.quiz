package com.opencourse.quiz;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import com.opencourse.quiz.dtos.TakeAnswerDto;

public class UtilTest {
    

    @Test
    public void testConparator(){
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime yesterday=LocalDateTime.now().minusDays(1);
        LocalDateTime beforeYesterday=LocalDateTime.now().minusDays(2);
        List<LocalDateTime> dates=List.of(today,yesterday,beforeYesterday);

        Optional<LocalDateTime> ordered=dates.stream()
        .sorted((one,two)->one.compareTo(two))
        .findFirst();

        assertEquals(beforeYesterday.getDayOfWeek(), ordered.get().getDayOfWeek());

    }


    @Test
    public void testFilters(){
        TakeAnswerDto dto1=new TakeAnswerDto(1L,true);
        TakeAnswerDto dto2=new TakeAnswerDto(2L,false);
        TakeAnswerDto dto3=new TakeAnswerDto(3L,false);
        TakeAnswerDto dto4=new TakeAnswerDto(4L,false);
        List<TakeAnswerDto> list=List.of(dto1,dto2,dto3,dto4);
        TakeAnswerDto expected=list
        .stream()
        .filter((a)->a.getAnswerId()==1L)
        .findFirst()
        .get();

        assertTrue(expected.isChecked());
    }
}
