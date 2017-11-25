package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffSide;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiffService {

    @Autowired
    private DiffPairRepository diffPairRepository;

    public void save(Long diffId, String diffContent, DiffSide diffSide){
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

    public DiffResultDTO runDifferentiator(Long diffId) {
        DiffResultDTO diffResultDTO;
        DiffPair diffPair = diffPairRepository.findOne(diffId);
        if(diffPair != null){
            diffResultDTO = new DiffResultDTO();
            diffResultDTO.setDiffPair(diffPair);
            if(!diffPair.getLeftSide().isEmpty() &&
                    !diffPair.getRightSide().isEmpty()){
                diffResultDTO.setResult("Diff result!");
            } else {
                diffResultDTO = null;
            }
        } else {
            diffResultDTO = null;
        }
        return diffResultDTO;
    }
}