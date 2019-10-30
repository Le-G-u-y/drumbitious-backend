import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExcercise } from 'app/shared/model/excercise.model';
import { ExcerciseService } from './excercise.service';

@Component({
  selector: 'jhi-excercise-delete-dialog',
  templateUrl: './excercise-delete-dialog.component.html'
})
export class ExcerciseDeleteDialogComponent {
  excercise: IExcercise;

  constructor(protected excerciseService: ExcerciseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.excerciseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'excerciseListModification',
        content: 'Deleted an excercise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-excercise-delete-popup',
  template: ''
})
export class ExcerciseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excercise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExcerciseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.excercise = excercise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/excercise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/excercise', { outlets: { popup: null } }]);
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
