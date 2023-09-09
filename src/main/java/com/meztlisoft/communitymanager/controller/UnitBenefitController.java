package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.entity.UnitBenefitEntity;
import com.meztlisoft.communitymanager.service.UnitBenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benefit")
@RequiredArgsConstructor
public class UnitBenefitController {


    private final UnitBenefitService unitBenefitService;

    @GetMapping("/all")
    public ResponseEntity<List<UnitBenefitEntity>> getAll() {
        return ResponseEntity.ok(unitBenefitService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitBenefitEntity> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(unitBenefitService.getById(id));
    }
}
