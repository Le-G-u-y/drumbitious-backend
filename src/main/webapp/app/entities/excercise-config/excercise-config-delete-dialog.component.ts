import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';
import { ExcerciseConfigService } from './excercise-config.service';

@Component({
  selector: 'jhi-excercise-config-delete-dialog',
  templateUrl: './excercise-config-delete-dialog.component.html'
})
export class ExcerciseConfigDeleteDialogComponent {
  excerciseConfig: IExcerciseConfig;

  constructor(
    protected excerciseConfigService: ExcerciseConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.excerciseConfigService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'excerciseConfigListModification',
        content: 'Deleted an excerciseConfig'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-excercise-config-delete-popup',
  template: ''
})
export class ExcerciseConfigDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excerciseConfig }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExcerciseConfigDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.excerciseConfig = excerciseConfig;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/excercise-config', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/excercise-config', { outlets: { popup: null } }]);
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
