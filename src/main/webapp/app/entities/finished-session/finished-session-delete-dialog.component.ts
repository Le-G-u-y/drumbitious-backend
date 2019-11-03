import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinishedSession } from 'app/shared/model/finished-session.model';
import { FinishedSessionService } from './finished-session.service';

@Component({
  selector: 'jhi-finished-session-delete-dialog',
  templateUrl: './finished-session-delete-dialog.component.html'
})
export class FinishedSessionDeleteDialogComponent {
  finishedSession: IFinishedSession;

  constructor(
    protected finishedSessionService: FinishedSessionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.finishedSessionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'finishedSessionListModification',
        content: 'Deleted an finishedSession'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-finished-session-delete-popup',
  template: ''
})
export class FinishedSessionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedSession }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FinishedSessionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.finishedSession = finishedSession;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/finished-session', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/finished-session', { outlets: { popup: null } }]);
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
