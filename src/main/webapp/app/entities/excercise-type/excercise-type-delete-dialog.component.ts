import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExcerciseType } from 'app/shared/model/excercise-type.model';
import { ExcerciseTypeService } from './excercise-type.service';

@Component({
  selector: 'jhi-excercise-type-delete-dialog',
  templateUrl: './excercise-type-delete-dialog.component.html'
})
export class ExcerciseTypeDeleteDialogComponent {
  excerciseType: IExcerciseType;

  constructor(
    protected excerciseTypeService: ExcerciseTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.excerciseTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'excerciseTypeListModification',
        content: 'Deleted an excerciseType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-excercise-type-delete-popup',
  template: ''
})
export class ExcerciseTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excerciseType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExcerciseTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.excerciseType = excerciseType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/excercise-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/excercise-type', { outlets: { popup: null } }]);
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
