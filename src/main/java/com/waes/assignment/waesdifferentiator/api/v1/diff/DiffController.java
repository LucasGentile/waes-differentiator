package com.waes.assignment.waesdifferentiator.api.v1.diff;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lucas on 23/11/2017.
 */

@RestController
@RequestMapping("/v1")
@Api(value = "diff")
public class DiffController {

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/left")
    public ResponseEntity addLeft(@PathVariable String id) {
        return new ResponseEntity<>("left side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/right")
    public ResponseEntity addRight(@PathVariable String id) {
        return new ResponseEntity<>("right side id = " + id, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/diff/{id}")
    public ResponseEntity getDiff(@PathVariable String id) {
        return new ResponseEntity<>("compare data from id = " + id, HttpStatus.OK);
    }
}
