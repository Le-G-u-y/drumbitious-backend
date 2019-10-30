import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SkillType } from 'app/shared/model/skill-type.model';
import { SkillTypeService } from './skill-type.service';
import { SkillTypeComponent } from './skill-type.component';
import { SkillTypeDetailComponent } from './skill-type-detail.component';
import { SkillTypeUpdateComponent } from './skill-type-update.component';
import { SkillTypeDeletePopupComponent } from './skill-type-delete-dialog.component';
import { ISkillType } from 'app/shared/model/skill-type.model';

@Injectable({ providedIn: 'root' })
export class SkillTypeResolve implements Resolve<ISkillType> {
  constructor(private service: SkillTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISkillType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SkillType>) => response.ok),
        map((skillType: HttpResponse<SkillType>) => skillType.body)
      );
    }
    return of(new SkillType());
  }
}

export const skillTypeRoute: Routes = [
  {
    path: '',
    component: SkillTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.skillType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SkillTypeDetailComponent,
    resolve: {
      skillType: SkillTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.skillType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SkillTypeUpdateComponent,
    resolve: {
      skillType: SkillTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.skillType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SkillTypeUpdateComponent,
    resolve: {
      skillType: SkillTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.skillType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const skillTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SkillTypeDeletePopupComponent,
    resolve: {
      skillType: SkillTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.skillType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
