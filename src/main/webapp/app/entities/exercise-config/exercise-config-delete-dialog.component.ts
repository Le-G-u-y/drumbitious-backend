import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExerciseConfig } from 'app/shared/model/exercise-config.model';
import { ExerciseConfigService } from './exercise-config.service';

@Component({
  selector: 'jhi-exercise-config-delete-dialog',
  templateUrl: './exercise-config-delete-dialog.component.html'
})
export class ExerciseConfigDeleteDialogComponent {
  exerciseConfig: IExerciseConfig;

  constructor(
    protected exerciseConfigService: ExerciseConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.exerciseConfigService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'exerciseConfigListModification',
        content: 'Deleted an exerciseConfig'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-exercise-config-delete-popup',
  template: ''
})
export class ExerciseConfigDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exerciseConfig }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExerciseConfigDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.exerciseConfig = exerciseConfig;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/exercise-config', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/exercise-config', { outlets: { popup: null } }]);
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
