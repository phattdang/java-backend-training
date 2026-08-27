package com.example.P07_CascadeOrphanRemoval.controller;

import com.example.P07_CascadeOrphanRemoval.alldemo.service.AllDemoService;
import com.example.P07_CascadeOrphanRemoval.dbcascadedemo.DatabaseCascadeDemoService;
import com.example.P07_CascadeOrphanRemoval.dto.*;
import com.example.P07_CascadeOrphanRemoval.mergedemo.service.MergeDemoService;
import com.example.P07_CascadeOrphanRemoval.nocascadedemo.service.NoCascadeDemoService;
import com.example.P07_CascadeOrphanRemoval.orphandemo.service.OrphanDemoService;
import com.example.P07_CascadeOrphanRemoval.persistdemo.service.PersistDemoService;
import com.example.P07_CascadeOrphanRemoval.removedemo.service.RemoveDemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/part9")
@RequiredArgsConstructor
public class CascadePracticeController {
    private final PersistDemoService persistDemoService;
    private final MergeDemoService mergeDemoService;
    private final RemoveDemoService removeDemoService;
    private final AllDemoService allDemoService;
    private final OrphanDemoService orphanDemoService;
    private final NoCascadeDemoService noCascadeDemoService;
    private final DatabaseCascadeDemoService databaseCascadeDemoService;

    // Part 9.1 + 9.2 + 9.6
    // Demonstrates CascadeType.PERSIST from Order parent to children; only Order is explicitly persisted.
    @PostMapping("/persist/orders")
    public CascadePersistResult persistParentOnly(@Valid @RequestBody CreateOrderRequest request) {
        return persistDemoService.persistParentOnly(request);
    }

    // Part 9.3
    // Demonstrates CascadeType.MERGE from a detached parent to detached children and reports managed state.
    @PostMapping("/merge/orders")
    public CascadeMergeResult mergeDetachedGraph(@Valid @RequestBody CreateOrderRequest request) {
        return mergeDemoService.mergeDetachedGraph(request);
    }

    // Part 9.4 setup
    // Creates a REMOVE-only graph; children are explicitly persisted because REMOVE does not include PERSIST.
    @PostMapping("/remove/orders")
    public CascadePersistResult createRemoveDemo(@Valid @RequestBody CreateOrderRequest request) {
        return removeDemoService.create(request);
    }

    // Part 9.4 + 9.10 Case B
    // Demonstrates CascadeType.REMOVE: deleting the parent removes every associated child.
    @DeleteMapping("/remove/orders/{parentId}")
    public CascadeRemoveResult removeParent(@PathVariable Long parentId) {
        return removeDemoService.removeParent(parentId);
    }

    // Part 9.5 + 9.7
    // Demonstrates convenient ALL on lifecycle-owned OrderItems; ALL must not be chosen by cardinality alone.
    @PostMapping("/all/orders")
    public CascadePersistResult persistAllGraph(@Valid @RequestBody CreateOrderRequest request) {
        return allDemoService.persistOwnedGraph(request);
    }

    // Part 9.8 setup
    // Creates an orphan-removal graph explicitly so setup does not hide the behavior under CascadeType.ALL.
    @PostMapping("/orphan/orders")
    public CascadePersistResult createOrphanDemo(@Valid @RequestBody CreateOrderRequest request) {
        return orphanDemoService.create(request);
    }

    // Part 9.8
    // Removing one child from Order.items deletes that orphan while the parent and other children remain.
    @DeleteMapping("/orphan/orders/{parentId}/items/{childId}")
    public OrphanRemovalResult removeOrphan(
            @PathVariable Long parentId, @PathVariable Long childId) {
        return orphanDemoService.removeOneChild(parentId, childId);
    }

    // Part 9.10 Case A setup
    // Creates a relationship with no cascade so the FK-protected parent-delete experiment is isolated.
    @PostMapping("/no-cascade/orders")
    public CascadePersistResult createNoCascadeDemo(@Valid @RequestBody CreateOrderRequest request) {
        return noCascadeDemoService.create(request);
    }

    // Part 9.10 Case A
    // Attempts parent deletion without REMOVE cascade and converts the expected FK failure into an explanatory DTO.
    @DeleteMapping("/no-cascade/orders/{parentId}")
    public NoCascadeRemoveResult attemptNoCascadeDelete(@PathVariable Long parentId) {
        return noCascadeDemoService.attemptParentDelete(parentId);
    }

    // Part 9.9
    // Compares parent-death propagation (REMOVE) with deleting a child abandoned by its parent (orphanRemoval).
    @GetMapping("/remove-vs-orphan")
    public ConceptComparisonResult compareRemoveAndOrphan() {
        return new ConceptComparisonResult(
                "Parent is removed", "REMOVE propagates to all children",
                "One child is removed from the parent relationship", "Only that orphan child is deleted",
                "Cascade REMOVE: Parent dies -> Child dies. orphanRemoval: Child is abandoned -> Child dies.");
    }

    // Part 9.11
    // Uses direct JDBC DELETE to prove PostgreSQL FK ON DELETE CASCADE works without JPA cascade.
    @PostMapping("/database-cascade/orders")
    public DatabaseCascadeResult databaseCascade(@Valid @RequestBody CreateOrderRequest request) {
        return databaseCascadeDemoService.directSqlDelete(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(IllegalArgumentException exception) {
        return Map.of("error", exception.getMessage());
    }
}
