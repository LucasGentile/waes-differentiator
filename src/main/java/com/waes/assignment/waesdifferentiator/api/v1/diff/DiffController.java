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
    public ResponseEntity getDiffPair(@PathVariable Long id) {
        DiffPair diffPair = diffService.findDiffPair(id);
        if(diffPair != null){
            return new ResponseEntity<>(diffPair, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Diff pair with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/left")
    public ResponseEntity addLeft(@PathVariable Long id, @Validated @RequestBody DiffSideDTO diffSideContent) {
        diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.LEFT);
        return new ResponseEntity<>("left side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/right")
    public ResponseEntity addRight(@PathVariable Long id, @Validated @RequestBody DiffSideDTO diffSideContent) {
        diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.RIGHT);
        return new ResponseEntity<>("right side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/diff/{id}")
    public ResponseEntity getDiff(@PathVariable Long id) {
        DiffResultDTO diffResultDTO;
        try {
            diffResultDTO = diffService.runDifferentiator(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(diffResultDTO, HttpStatus.OK);
    }
}
