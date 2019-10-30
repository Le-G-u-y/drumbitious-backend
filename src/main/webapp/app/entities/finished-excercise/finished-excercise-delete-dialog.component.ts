import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';
import { FinishedExcerciseService } from './finished-excercise.service';

@Component({
  selector: 'jhi-finished-excercise-delete-dialog',
  templateUrl: './finished-excercise-delete-dialog.component.html'
})
export class FinishedExcerciseDeleteDialogComponent {
  finishedExcercise: IFinishedExcercise;

  constructor(
    protected finishedExcerciseService: FinishedExcerciseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.finishedExcerciseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'finishedExcerciseListModification',
        content: 'Deleted an finishedExcercise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-finished-excercise-delete-popup',
  template: ''
})
export class FinishedExcerciseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedExcercise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FinishedExcerciseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.finishedExcercise = finishedExcercise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/finished-excercise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/finished-excercise', { outlets: { popup: null } }]);
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
