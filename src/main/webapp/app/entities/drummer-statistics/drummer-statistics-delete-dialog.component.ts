import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDrummerStatistics } from 'app/shared/model/drummer-statistics.model';
import { DrummerStatisticsService } from './drummer-statistics.service';

@Component({
  selector: 'jhi-drummer-statistics-delete-dialog',
  templateUrl: './drummer-statistics-delete-dialog.component.html'
})
export class DrummerStatisticsDeleteDialogComponent {
  drummerStatistics: IDrummerStatistics;

  constructor(
    protected drummerStatisticsService: DrummerStatisticsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.drummerStatisticsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'drummerStatisticsListModification',
        content: 'Deleted an drummerStatistics'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-drummer-statistics-delete-popup',
  template: ''
})
export class DrummerStatisticsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ drummerStatistics }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DrummerStatisticsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.drummerStatistics = drummerStatistics;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/drummer-statistics', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/drummer-statistics', { outlets: { popup: null } }]);
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
