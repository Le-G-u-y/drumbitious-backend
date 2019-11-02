import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { II18nText } from 'app/shared/model/i-18-n-text.model';
import { I18nTextService } from './i-18-n-text.service';

@Component({
  selector: 'jhi-i-18-n-text-delete-dialog',
  templateUrl: './i-18-n-text-delete-dialog.component.html'
})
export class I18nTextDeleteDialogComponent {
  i18nText: II18nText;

  constructor(protected i18nTextService: I18nTextService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.i18nTextService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'i18nTextListModification',
        content: 'Deleted an i18nText'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-i-18-n-text-delete-popup',
  template: ''
})
export class I18nTextDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ i18nText }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(I18nTextDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.i18nText = i18nText;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/i-18-n-text', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/i-18-n-text', { outlets: { popup: null } }]);
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
