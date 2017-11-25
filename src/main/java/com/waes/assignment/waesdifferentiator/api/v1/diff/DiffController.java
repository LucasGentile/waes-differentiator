package com.waes.assignment.waesdifferentiator.api.v1.diff;

import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffSide;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffResultDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffSideDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class DiffController {

    @Autowired
    private DiffService diffService;

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/left")
    public ResponseEntity addLeft(@PathVariable Long id, @RequestBody DiffSideDTO diffSideContent) {
        diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.LEFT);
        return new ResponseEntity<>("left side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/right")
    public ResponseEntity addRight(@PathVariable Long id, @RequestBody DiffSideDTO diffSideContent) {
        diffService.save(id, diffSideContent.getDiffSideContent(), DiffSide.RIGHT);
        return new ResponseEntity<>("right side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/diff/{id}")
    public ResponseEntity getDiff(@PathVariable Long id) {
        DiffResultDTO diffResultDTO = diffService.runDifferentiator(id);
        return new ResponseEntity<>(diffResultDTO, HttpStatus.OK);
    }
}
