package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffSide;
import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffSideDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class DiffController {

    @Autowired
    private DiffService diffService;

    @RequestMapping(method = RequestMethod.GET, value = "/diff/diffPair/{id}")
    public ResponseEntity<DiffPair> getDiffPair(@PathVariable Long id) {
        DiffPair diffPair = diffService.findDiffPair(id);
        if(diffPair != null){
            return new ResponseEntity<>(diffPair, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(diffPair, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/left")
    public ResponseEntity<DiffPair> saveLeft(@PathVariable Long id, @Validated @RequestBody DiffSideDTO diffSideContent) {

        DiffPair savedDiffPair = null;
        try{
            savedDiffPair = diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.LEFT);

            return new ResponseEntity<>(savedDiffPair, HttpStatus.CREATED);

        } catch (Exception e){
            return new ResponseEntity<>(savedDiffPair, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/right")
    public ResponseEntity<DiffPair> saveRight(@PathVariable Long id, @Validated @RequestBody DiffSideDTO diffSideContent) {
        DiffPair savedDiffPair = null;
        try{
            savedDiffPair = diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.RIGHT);

            return new ResponseEntity<>(savedDiffPair, HttpStatus.CREATED);

        } catch (Exception e){
            return new ResponseEntity<>(savedDiffPair, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}")
    public ResponseEntity<DiffResultDTO> getDiff(@PathVariable Long id) {
        DiffResultDTO diffResultDTO = null;
        try {
            diffResultDTO = diffService.runDifferentiator(id);

            return new ResponseEntity<>(diffResultDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(diffResultDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
