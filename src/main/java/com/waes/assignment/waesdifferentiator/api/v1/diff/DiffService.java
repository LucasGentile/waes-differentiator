package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffResult;
import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffSide;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
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

    void save(Long diffId, String diffContent, DiffSide diffSide){
        DiffPair diffPair = diffPairRepository.findOne(diffId);
        if(diffPair == null){
            diffPair = new DiffPair();
            diffPair.setDiffId(diffId);
        }

        if(diffSide.equals(DiffSide.LEFT))
            diffPair.setLeftSide(diffContent);
        else if(diffSide.equals(DiffSide.RIGHT))
            diffPair.setRightSide(diffContent);

        diffPairRepository.save(diffPair);
    }

    public DiffResultDTO runDifferentiator(Long diffId) throws Exception {
        DiffPair diffPair = diffPairRepository.findOne(diffId);
        if(diffPair == null){
            throw new Exception("DiffPair with id " + diffId + " not found.");
        } else {
            if(diffPair.getLeftSide() == null){
                throw new Exception("DiffPair with id " + diffId + " missing LEFT side.");
            }
            if(diffPair.getRightSide() == null){
                throw new Exception("DiffPair with id " + diffId + " missing RIGHT side.");
            }

            return diff(diffPair);
        }
    }

    private DiffResultDTO diff(DiffPair diffPair) {
        DiffResultDTO diffResultDTO = new DiffResultDTO();
        diffResultDTO.setDiffPair(diffPair);

        char[] leftSide = diffPair.getLeftSide().toCharArray();
        char[] rightSide = diffPair.getRightSide().toCharArray();

        if(Arrays.equals(leftSide, rightSide)){
            diffResultDTO.setDiffResult(DiffResult.EQUAL);
        } else if(leftSide.length != rightSide.length){
            diffResultDTO.setDiffResult(DiffResult.DIFFERENT_SIZE);
        } else {
            diffResultDTO.setDiffResult(DiffResult.DIFFERENT_SIZE);

            List<Integer> diffList = new ArrayList<>();
            for (int offset = 0; offset < leftSide.length; offset++){
                if(leftSide[offset] != rightSide[offset]){
                    diffList.add(offset);
                }
            }

            diffResultDTO.setDiffOffsets(diffList.toString());
        }
        return diffResultDTO;
    }
}