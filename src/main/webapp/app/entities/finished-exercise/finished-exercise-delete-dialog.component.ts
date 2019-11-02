import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';
import { FinishedExerciseService } from './finished-exercise.service';

@Component({
  selector: 'jhi-finished-exercise-delete-dialog',
  templateUrl: './finished-exercise-delete-dialog.component.html'
})
export class FinishedExerciseDeleteDialogComponent {
  finishedExercise: IFinishedExercise;

  constructor(
    protected finishedExerciseService: FinishedExerciseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.finishedExerciseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'finishedExerciseListModification',
        content: 'Deleted an finishedExercise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-finished-exercise-delete-popup',
  template: ''
})
export class FinishedExerciseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedExercise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FinishedExerciseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.finishedExercise = finishedExercise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/finished-exercise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/finished-exercise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
