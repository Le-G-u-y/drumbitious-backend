import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISkillType } from 'app/shared/model/skill-type.model';
import { SkillTypeService } from './skill-type.service';

@Component({
  selector: 'jhi-skill-type-delete-dialog',
  templateUrl: './skill-type-delete-dialog.component.html'
})
export class SkillTypeDeleteDialogComponent {
  skillType: ISkillType;

  constructor(protected skillTypeService: SkillTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.skillTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'skillTypeListModification',
        content: 'Deleted an skillType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-skill-type-delete-popup',
  template: ''
})
export class SkillTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ skillType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SkillTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.skillType = skillType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/skill-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/skill-type', { outlets: { popup: null } }]);
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
