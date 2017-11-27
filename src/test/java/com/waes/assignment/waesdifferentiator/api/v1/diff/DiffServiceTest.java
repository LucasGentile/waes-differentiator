package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffResultType;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.waes.assignment.waesdifferentiator.api.v1.diff.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 *
 * Test classes naming patter:
 *     methodCalled_input_output
 *
 */
@RunWith(SpringRunner.class)
public class DiffServiceTest {

    @TestConfiguration
    static class DiffServiceTestContextConfiguration {

        @Bean
        public DiffService diffService() {
            return new DiffService();
        }

    }

    @Autowired
    private DiffService diffService;

    @MockBean
    private DiffPairRepository diffPairRepository;

    @Test(expected = NotFoundException.class)
    public void executeRunDifferentiator_DiffPairNotFound_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        when(diffPairRepository.findOne(diffId)).thenReturn(null);

        diffService.runDifferentiator(diffId);
    }

    @Test(expected = NotFoundException.class)
    public void executeRunDifferentiator_DiffPairLeftSideNull_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPairLeftSideNull = createDiffPair(diffId, null, encodeBase64String("test"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPairLeftSideNull);

        diffService.runDifferentiator(diffId);
    }

    @Test(expected = NotFoundException.class)
    public void executeRunDifferentiator_DifPairRightSideNull_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPairRightSideNull = createDiffPair(diffId, encodeBase64String("test"), null);

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPairRightSideNull);

        diffService.runDifferentiator(diffId);
    }

    @Test
    public void executeRunDifferentiator_DiffPairEqualSides_DiffResultTypeEqual() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("equal_string_to_be_diff-ed"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.EQUAL);
        assertNull(diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairSidesDifferentLength_DiffResultTypeDifferentLength() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("different_length_string_to_be_diff-ed"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.DIFFERENT_LENGTH);
        assertNull(diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairOffsetLengthAtFirstChars_DiffResultTypeDiff() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("diffng_to_be_diff-ed"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(DiffResultType.DIFF, diffResultDTO.getDiffResultType());
        assertEquals("[0-5]", diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairOffsetLengthAtLastChars_DiffResultTypeDiff() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_difdiff"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.DIFF);
        assertEquals("[22-23, 25-26]", diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairOffsetLengthAllChars_DiffResultTypeDiff() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("12345678910987654321"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.DIFF);
        assertEquals("[0-26]", diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairSingleOffsetAtFirstChar_DiffResultTypeDiff() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("ktring_to_be_diff-ed"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.DIFF);
        assertEquals("[0]", diffResultDTO.getDiffOffsets());
    }

    @Test
    public void executeRunDifferentiator_DiffPairSingleOffsetAtLastChar_DiffResultTypeDiff() throws Exception {
        Long diffId = getRandomId();

        DiffPair diffPair = createDiffPair(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_diff-ee"));

        when(diffPairRepository.findOne(diffId)).thenReturn(diffPair);

        DiffResultDTO diffResultDTO = diffService.runDifferentiator(diffId);

        assertEquals(diffPair.getLeftSide(), diffResultDTO.getDiffPair().getLeftSide());
        assertEquals(diffPair.getRightSide(), diffResultDTO.getDiffPair().getRightSide());
        assertEquals(diffResultDTO.getDiffResultType(), DiffResultType.DIFF);
        assertEquals("[26]", diffResultDTO.getDiffOffsets());
    }
}
