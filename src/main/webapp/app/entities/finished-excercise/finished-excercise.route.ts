import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FinishedExcercise } from 'app/shared/model/finished-excercise.model';
import { FinishedExcerciseService } from './finished-excercise.service';
import { FinishedExcerciseComponent } from './finished-excercise.component';
import { FinishedExcerciseDetailComponent } from './finished-excercise-detail.component';
import { FinishedExcerciseUpdateComponent } from './finished-excercise-update.component';
import { FinishedExcerciseDeletePopupComponent } from './finished-excercise-delete-dialog.component';
import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';

@Injectable({ providedIn: 'root' })
export class FinishedExcerciseResolve implements Resolve<IFinishedExcercise> {
  constructor(private service: FinishedExcerciseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFinishedExcercise> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FinishedExcercise>) => response.ok),
        map((finishedExcercise: HttpResponse<FinishedExcercise>) => finishedExcercise.body)
      );
    }
    return of(new FinishedExcercise());
  }
}

export const finishedExcerciseRoute: Routes = [
  {
    path: '',
    component: FinishedExcerciseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExcercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FinishedExcerciseDetailComponent,
    resolve: {
      finishedExcercise: FinishedExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExcercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FinishedExcerciseUpdateComponent,
    resolve: {
      finishedExcercise: FinishedExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExcercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FinishedExcerciseUpdateComponent,
    resolve: {
      finishedExcercise: FinishedExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExcercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const finishedExcercisePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FinishedExcerciseDeletePopupComponent,
    resolve: {
      finishedExcercise: FinishedExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExcercise.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
