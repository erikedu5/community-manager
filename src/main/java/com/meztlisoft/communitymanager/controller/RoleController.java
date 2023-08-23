package com.meztlisoft.communitymanager.controller;

import com.meztlisoft.communitymanager.entity.RoleEntity;
import com.meztlisoft.communitymanager.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/all")
    public ResponseEntity<List<RoleEntity>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> getById(@PathVariable(name = "id") final long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }


}
