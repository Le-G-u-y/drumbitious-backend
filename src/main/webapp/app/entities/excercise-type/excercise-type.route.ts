import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ExcerciseType } from 'app/shared/model/excercise-type.model';
import { ExcerciseTypeService } from './excercise-type.service';
import { ExcerciseTypeComponent } from './excercise-type.component';
import { ExcerciseTypeDetailComponent } from './excercise-type-detail.component';
import { ExcerciseTypeUpdateComponent } from './excercise-type-update.component';
import { ExcerciseTypeDeletePopupComponent } from './excercise-type-delete-dialog.component';
import { IExcerciseType } from 'app/shared/model/excercise-type.model';

@Injectable({ providedIn: 'root' })
export class ExcerciseTypeResolve implements Resolve<IExcerciseType> {
  constructor(private service: ExcerciseTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExcerciseType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ExcerciseType>) => response.ok),
        map((excerciseType: HttpResponse<ExcerciseType>) => excerciseType.body)
      );
    }
    return of(new ExcerciseType());
  }
}

export const excerciseTypeRoute: Routes = [
  {
    path: '',
    component: ExcerciseTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExcerciseTypeDetailComponent,
    resolve: {
      excerciseType: ExcerciseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExcerciseTypeUpdateComponent,
    resolve: {
      excerciseType: ExcerciseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExcerciseTypeUpdateComponent,
    resolve: {
      excerciseType: ExcerciseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const excerciseTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExcerciseTypeDeletePopupComponent,
    resolve: {
      excerciseType: ExcerciseTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excerciseType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
