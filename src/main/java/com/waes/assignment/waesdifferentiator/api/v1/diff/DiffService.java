package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffResultType;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffSide;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
class DiffService {

    @Autowired
    private DiffPairRepository diffPairRepository;

    DiffPair findDiffPair(Long diffId){
        return diffPairRepository.findOne(diffId);
    }

    /**
     *
     * @param diffId DiffPair id
     * @param diffContent content to be saved
     * @param diffSide enum saying if it is RIGHT or LEFT side
     * @return
     */
    DiffPair save(Long diffId, String diffContent, DiffSide diffSide){
        DiffPair diffPair = diffPairRepository.findOne(diffId);
        if(diffPair == null){
            diffPair = new DiffPair();
            diffPair.setDiffId(diffId);
        }

        if(diffSide.equals(DiffSide.LEFT))
            diffPair.setLeftSide(diffContent);
        else if(diffSide.equals(DiffSide.RIGHT))
            diffPair.setRightSide(diffContent);

        return diffPairRepository.save(diffPair);
    }

    DiffResultDTO runDifferentiator(Long diffId) throws Exception {
        DiffPair diffPair = diffPairRepository.findOne(diffId);
        if(diffPair == null){
            throw new NotFoundException("DiffPair with id " + diffId + " not found.");
        } else {
            if(diffPair.getLeftSide() == null){
                throw new NotFoundException("DiffPair with id " + diffId + " missing LEFT side.");
            }
            if(diffPair.getRightSide() == null){
                throw new NotFoundException("DiffPair with id " + diffId + " missing RIGHT side.");
            }

            return diff(diffPair);
        }
    }

    /**
     * Get the offsets where there are differences between LEFT and RIGHT sides from the DiffPair
     *
     * @param diffPair element containing the LEFT and RIGHT side strings the will be diff-ed
     * @return diffResultDTO element containing the DiffPair element that was diff-ed, the ResultType and also a formatted string with the offsets
     */
    private DiffResultDTO diff(DiffPair diffPair) {
        DiffResultDTO diffResultDTO = new DiffResultDTO();
        diffResultDTO.setDiffPair(diffPair);

        char[] leftSide = diffPair.getLeftSide().toCharArray();
        char[] rightSide = diffPair.getRightSide().toCharArray();

        if(Arrays.equals(leftSide, rightSide)){
            diffResultDTO.setDiffResultType(DiffResultType.EQUAL);
        } else if(leftSide.length != rightSide.length){
            diffResultDTO.setDiffResultType(DiffResultType.DIFFERENT_LENGTH);
        } else {
            diffResultDTO.setDiffResultType(DiffResultType.DIFF);

            List<Integer> diffList = new ArrayList<>();
            for (int offset = 0; offset < leftSide.length; offset++){
                if(leftSide[offset] != rightSide[offset]){
                    diffList.add(offset);
                }
            }

            diffResultDTO.setDiffOffsets(diffResultFormatter(diffList));
        }

        return diffResultDTO;
    }

    /**
     * Format the offset output .
     *
     * When it's a single offset in the middle of the string, it will be displayed as a single number:
     *      [9]
     * If it's more than one offset in a sequence it will be displayed showing the interval:
     *      [3-9]
     *
     * An example of a complete formatted output would be:
     *      [1, 3-6, 7]
     *
     * @param diffList list containing all the offsets
     * @return Formatted message
     */
    private String diffResultFormatter(List<Integer> diffList) {
        StringBuilder diffs = new StringBuilder("[");

        for (int offsetPosition = 0; offsetPosition < diffList.size(); offsetPosition++) {

            Integer currentOffset = diffList.get(offsetPosition);
            if (offsetPosition > 0) {
                Integer previousOffset = diffList.get(offsetPosition-1);
                if((currentOffset-previousOffset) == 1) {
                    if(diffs.charAt((diffs.length()-1)) != '-'){
                        if(offsetPosition == (diffList.size()-1)){
                            diffs.append("-").append(currentOffset);
                        }else {
                            diffs.append("-");
                        }
                    } else if(offsetPosition == (diffList.size()-1)){
                        diffs.append(currentOffset);
                    }
                } else {
                    if(diffs.charAt((diffs.length()-1)) == '-'){
                        diffs.append(previousOffset).append(", ").append(currentOffset);
                    } else {
                        diffs.append(", ").append(currentOffset);
                    }
                }
            } else {
                diffs.append(currentOffset);
            }
        }
        diffs.append("]");

        return diffs.toString();
    }
}